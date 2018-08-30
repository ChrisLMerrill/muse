package org.musetest.core.resource.storage;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.util.*;
import org.reflections.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FolderIntoMemoryResourceStorage extends InMemoryResourceStorage implements FilesystemStorage
    {
    public FolderIntoMemoryResourceStorage(File folder)
        {
        _folder = folder;
        locateClasspaths();
        loadAllResources();
        }

    @Override
    public File getBaseLocation()
        {
        return _folder;
        }

    /**
     * Add a new resource.
     */
    @Override
    public ResourceToken addResource(MuseResource resource) throws IOException
        {
        if (getResource(resource.getId()) != null)
            throw new IllegalArgumentException("Resource with already exists with the same ID: " + resource.getId());

        String error = saveResource(resource);
        if (error != null)
            throw new IOException(error);
        return super.addResource(resource);
        }

    @Override
    public boolean removeResource(ResourceToken token)
        {
        if (getResource(token) != null)
            {
            FileResourceOrigin origin = (FileResourceOrigin) _origins.get(token.getResource());
            File file = origin.getFile();
            if (file.delete())
                return super.removeResource(token);
            }
        return false;
        }

    private void loadAllResources()
        {
        Runnable loader = () ->
            {
            File[] files = _folder.listFiles();
            if (files == null)
                return;
            for (File file : files)
                {
                if (file.isDirectory())
                	continue;
                FileResourceOrigin origin = new FileResourceOrigin(file);
                try
                    {
                    List<MuseResource> resources = ResourceFactory.createResources(origin, getFactoryLocator(), getClassLocator());
                    // don't do call the local add() method...that is for adding new resources.
                    for (MuseResource resource : resources)
                        {
                        super.addResource(resource);
                        _origins.put(resource, origin);
                        }
                    }
                catch (IOException e)
                    {
                    LOG.warn("Unable to load resource from origin: " + origin.getDescription(), e);
                    }
                }
            };
        ClassloaderRunner.executeWithClassloader(loader, getContextClassloader());
        }

    private void locateClasspaths()
        {
        List<File> class_locations = new ArrayList<>();
        _packages = new ArrayList<>();
        _packages.add("org.musetest");

        String[] paths = {"classes", "build/classes/java/main"};
        for (String path : paths)
            {
            File classes = new File(_folder, path);
            if (classes.exists())
                {
                LOG.debug("Adding folder to project classpath: " + classes.getAbsolutePath());
                class_locations.add(classes);
                findPackagesInFolders(_packages, classes, "");
                }
            }

        File[] jars = new File[0];
        File lib = new File(_folder, "lib");
        if (lib.exists() && lib.isDirectory())
            jars = lib.listFiles((dir, name) ->
                name.endsWith(".jar"));
        if (jars == null)
        	return;
        for (File jar : jars)
            {
            LOG.debug("Adding jar to project classpath: " + jar.getAbsolutePath());
            class_locations.add(jar);
            _packages.addAll(getPackages(jar));
            }
        _class_locations = class_locations;
        }

    private void findPackagesInFolders(List<String> packages, File folder, String base_package_name)
        {
        File[] subfolders = folder.listFiles();
        if (subfolders == null)
        	return;
        for (File subfolder : subfolders)
            {
            if (subfolder.isDirectory())
                {
                String package_name;
                if (base_package_name.length() == 0)
                    package_name = subfolder.getName();
                else
                    package_name = base_package_name + "." + subfolder.getName();
                packages.add(package_name);
                findPackagesInFolders(packages, subfolder, package_name);
                }
            }
        }

    private List<String> getPackages(File jar)
        {
        List<String> packages = new ArrayList<>();
        try (InputStream filestream = new FileInputStream(jar))
            {
            JarInputStream jarstream = new JarInputStream(filestream);
            final Attributes attributes = jarstream.getManifest().getMainAttributes();
            final String package_list = attributes.getValue("muse-package");
            if (package_list != null)
                {
                StringTokenizer tokenizer = new StringTokenizer(package_list, ",");
                while (tokenizer.hasMoreTokens())
                    packages.add(tokenizer.nextToken());
                }
            }
        catch (IOException e)
            {
            LOG.error("Unable to read jar for package spec: " + jar.getName());
            }
        return packages;
        }

    @Override
    public ClassLoader getContextClassloader()
        {
        if (_class_loader == null && _class_locations.size() > 0)
            {
            List<URL> urls = new ArrayList<>();
            for (File file : _class_locations)
                try
                    {
                    urls.add(file.toURI().toURL());
                    }
                catch (MalformedURLException e)
                    {
                    LOG.error(String.format("Unable to add %s to the context classloader", file.getAbsoluteFile()), e);
                    }
            URL[] url_array = urls.toArray(new URL[urls.size()]);
            _class_loader = new URLClassLoader(url_array, getClass().getClassLoader());

            StringBuilder package_string = new StringBuilder();
            for (String package_spec : _packages)
                {
                package_string.append(package_spec);
                package_string.append(",");
                }
            LOG.debug("Project context classloader will search in packages: " + package_string.toString());
            Object[] class_search_path = new Object[url_array.length + _packages.size()];
            System.arraycopy(url_array, 0, class_search_path, 0, url_array.length);
            for (int i = 0; i < _packages.size(); i++)
                class_search_path[url_array.length + i] = _packages.get(i);
            setClassLocator(new CustomClassLocator(_class_loader, new Reflections(class_search_path)));
            }
        return _class_loader;
        }

    @Override
    public String saveResource(MuseResource resource)
        {
        ResourceSerializer serializer = null;
        ResourceOrigin origin = _origins.get(resource);
        if (origin != null)
            serializer = origin.getSerializer();

        if (serializer == null)
            serializer = DefaultSerializers.get(resource);
        //noinspection ConstantConditions  The above is eventually expected to be able to return something not null.
        if (serializer == null)
            return "Unable to find a serializer for a " + resource.getType().getName();

        OutputStream outstream = null;
        try
            {
            File new_file = null;
            if (origin == null)
                {
                String filename = serializer.suggestFilename(resource);
                if (!(new FilenameValidator().isValid(filename)))
                    return "not a valid resource id";
                new_file = new File(_folder, filename);
                outstream = new FileOutputStream(new_file);
                }
            else
                outstream = origin.asOutputStream();

            serializer.writeToStream(resource, outstream, new TypeLocator(getClassLocator()));

            if (new_file != null)
                {
                // new resource...record the origin
                _origins.put(resource, new FileResourceOrigin(new_file));
                }
            return null;
            }
        catch (IOException e)
            {
            LOG.error("Unable to save the resource", e);
            return "Saving resource failed. Consult the diagnostic log";
            }
        finally
            {
            try { if (outstream != null) outstream.close(); } catch (IOException e) { /* nothing to do here */ }
            }
        }

/*
    @Override
    public ExtensionRegistry getExtensionRegistry()
        {
        return new ExtensionRegistry(new File(_folder, ExtensionRegistry.DEFAULT_FOLDER));
        }

*/
    private File _folder;
    private List<File> _class_locations = new ArrayList<>();
    private List<String> _packages = new ArrayList<>();
    private ClassLoader _class_loader = null;
    private Map<MuseResource, ResourceOrigin> _origins = new HashMap<>();

    private final static Logger LOG = LoggerFactory.getLogger(FolderIntoMemoryResourceStorage.class);
    }



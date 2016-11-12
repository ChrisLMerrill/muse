package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.resource.json.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.util.*;
import org.reflections.*;
import org.slf4j.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FolderIntoMemoryResourceStore extends InMemoryResourceStore
    {
    public FolderIntoMemoryResourceStore(File folder)
        {
        _folder = folder;
        locateClasspaths();
        loadAllResources();
        }

    /**
     * Add a new resource.
     */
    @Override
    public ResourceToken addResource(MuseResource resource)
        {
        // for a new resource, we need to setup the origin
        if (resource.getMetadata().getOrigin() == null)
            {
            resource.getMetadata().setSaver(new FromJsonFileResourceFactory());
            resource.getMetadata().setOrigin(new FileResourceOrigin(new File(_folder, resource.getMetadata().getId() + "." + resource.getMetadata().getSaver().getDefaultFileExtension())));
            }
        ResourceToken token = super.addResource(resource);
        saveResource(resource);
        return token;
        }

    @Override
    public boolean removeResource(ResourceToken token)
        {
        if (getResource(token) != null)
            {
            FileResourceOrigin origin = (FileResourceOrigin) token.getMetadata().getOrigin();
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
                FileResourceOrigin origin = new FileResourceOrigin(file);
                try
                    {
                    List<MuseResource> resources = ResourceFactory.createResources(origin, getFactoryLocator(), getClassLocator());
                    // don't do call the local add() method...that is for adding new resources.
                    resources.forEach(FolderIntoMemoryResourceStore.super::addResource);
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

        String[] paths = {"classes", "build/classes/main"};
        for (String path : paths)
            {
            File classes = new File(_folder, path);
            if (classes.exists())
                {
                LOG.debug("Adding to project classpath: " + classes.getAbsolutePath());
                class_locations.add(classes);
                }
            }

        File[] jars = new File[0];
        File lib = new File(_folder, "lib");
        if (lib.exists() && lib.isDirectory())
            jars = lib.listFiles((dir, name) ->
                {
                return name.endsWith(".jar");
                });
        for (File jar : jars)
            {
            LOG.debug("Adding to project classpath: " + jar.getAbsolutePath());
            class_locations.add(jar);
            }
        _class_locations = class_locations;
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
            _class_loader = new URLClassLoader(url_array);

            Object[] class_search_path = new Object[url_array.length + 1];
            System.arraycopy(url_array, 0, class_search_path, 0, url_array.length);
            class_search_path[url_array.length] = "org.musetest";
            setClassLocator(new CustomClassLocator(new Reflections(class_search_path)));
            }
        return _class_loader;
        }

    @Override
    public String saveResource(MuseResource resource)
        {
        MuseResourceSaver saver = resource.getMetadata().getSaver();
        if (saver == null)
            return "No saver configured for the resource :(";

        if (!saver.saveResource(resource, new TypeLocator(getClassLocator())))
            return "Saving resource failed. Consult the diagnostic log";

        return null;
        }

    private File _folder;
    private List<File> _class_locations = new ArrayList<>();
    private ClassLoader _class_loader = null;

    private final static Logger LOG = LoggerFactory.getLogger(FolderIntoMemoryResourceStore.class);
    }



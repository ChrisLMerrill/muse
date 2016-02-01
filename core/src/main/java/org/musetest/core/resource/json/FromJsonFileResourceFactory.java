package org.musetest.core.resource.json;

import com.fasterxml.jackson.databind.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.util.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // via reflection
public class FromJsonFileResourceFactory implements MuseResourceFactory, MuseResourceSaver
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ClassLocator classes) throws IOException
        {
        TypeLocator type_locator = new TypeLocator(classes);
        List<MuseResource> resources = new ArrayList<>();
        if (origin instanceof FileResourceOrigin)
            {
            FileInputStream instream = null;
            try
                {
                File file = ((FileResourceOrigin)origin).getFile();
                if (file.getName().endsWith(".json"))
                    {
                    instream = new FileInputStream(file);
                    createResources(origin, resources, type_locator);
                    }
                }
            catch (Exception e)
                {
                LOG.warn("Cannot load resource from origin: " + origin.getDescription() + " - " + e.getMessage());
                }
            finally
                {
                if (instream != null)
                    try { instream.close(); } catch (IOException e) { /* noop */ }
                }
            }
        else if (origin instanceof StreamResourceOrigin)
            {
            try
                {
                createResources(origin, resources, type_locator);
                }
            catch (Exception e)
                {
                // do nothing...it may not even be JSON in the stream.
                }
            }
        return resources;
        }

    private void createResources(ResourceOrigin origin, List<MuseResource> resources, TypeLocator type_locator) throws IOException
        {
        try
            {
            MuseResource resource = getMapper(type_locator).readValue(origin.asStream(), MuseResource.class);
            resource.getMetadata().setSaver(this);

            resources.add(resource);
            // TODO read multiple?
            }
        catch (Exception e)
            {
            LOG.error("Unable to read a resource from " + origin.getDescription() + " - " + e.getMessage());
            }
        }

    @Override
    public Boolean saveResource(MuseResource resource, TypeLocator type_locator)
        {
        ResourceOrigin origin = resource.getMetadata().getOrigin();
        String error_message = null;  // factories only return an error
        if (origin instanceof FileResourceOrigin)
            {
            File target_file = ((FileResourceOrigin) origin).getFile();
            try
                {
                FileOutputStream outstream = new FileOutputStream(target_file);
                getMapper(type_locator).writerWithDefaultPrettyPrinter().writeValue(outstream, resource);
                outstream.close();
                return true;
                }
            catch (Exception e)
                {
                LOG.error("Unable to save the resource to " + target_file.getPath() + " - " + e.getMessage(), e);

                return false;
                }
            }
        else
            {
            LOG.error("null or unrecognized file origin. Don't know where to save the resource");
            return false;
            }
        }

    private ObjectMapper getMapper(TypeLocator type_locator)
        {
        if (_mapper == null)
            _mapper = JsonMapperFactory.createMapper(type_locator);
        return _mapper;
        }

    @Override
    public String getDefaultFileExtension()
        {
        return "json";
        }

    private ObjectMapper _mapper = null;

    final static Logger LOG = LoggerFactory.getLogger(FromJsonFileResourceFactory.class);
    }



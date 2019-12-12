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
public class FromJsonFileResourceFactory implements MuseResourceFactory
    {
    @Override
    public List<MuseResource> createResources(ResourceOrigin origin, ClassLocator classes)
        {
        TypeLocator type_locator = new TypeLocator(classes);
        List<MuseResource> resources = new ArrayList<>();
        if (origin instanceof FileResourceOrigin)
            {
            try
                {
                File file = ((FileResourceOrigin)origin).getFile();
                // TODO Remove support for reading .json resources in a few months (from Dec 2019).
                // TODO At that time, also remove (revise?) the warning in NavigatorView (MuseIDE project)
                if (file.getName().endsWith(".json") || file.getName().endsWith(".muse"))
                    createResources(origin, resources, type_locator);
                }
            catch (Exception e)
                {
                LOG.warn("Cannot load resource from origin: " + origin.getDescription() + " - " + e.getMessage());
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

    private void createResources(ResourceOrigin origin, List<MuseResource> resources, TypeLocator type_locator)
        {
        try (InputStream instream = origin.asInputStream())
            {
            MuseResource resource = _serializer.readFromStream(instream, type_locator);
            origin.setSerializer(_serializer);

            resources.add(resource);
            // TODO read multiple?
            }
        catch (Exception e)
            {
            LOG.error("Unable to read a resource from " + origin.getDescription() + " - " + e.getMessage());
            }
        }

    private ObjectMapper getMapper(TypeLocator type_locator)
        {
        if (_mapper == null)
            _mapper = JsonMapperFactory.createMapper(type_locator);
        return _mapper;
        }

    private ObjectMapper _mapper = null;
    private JsonResourceSerializer _serializer = new JsonResourceSerializer();

    private final static Logger LOG = LoggerFactory.getLogger(FromJsonFileResourceFactory.class);
    }



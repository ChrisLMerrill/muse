package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.util.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceFactory
    {
    /**
     * Convenience method for unit tests.  Use the other createResources() to load with the correct classpaths for project-defined resources.
     *
     * @param origin Where to load the resources from
     * @return A list of resources loaded from the origin
     * @throws IOException If the resource cannot be read
     */
    public static List<MuseResource> createResources(ResourceOrigin origin) throws IOException
        {
        return createResources(origin, new FactoryLocator(DefaultClassLocator.get()), DefaultClassLocator.get());
        }

    public static List<MuseResource> createResources(ResourceOrigin origin, FactoryLocator factory_locator, ClassLocator locator) throws IOException
        {
        ResourceTypes resource_types = new ResourceTypes(locator);
        List<MuseResource> resources = new ArrayList<>();
        List<MuseResourceFactory> factories = factory_locator.findFactories(MuseResourceFactory.class);
        if (factories.isEmpty())
            LOG.info("No factories suitable for loading resources from " + origin.getDescription());
        else
            {
            for (MuseResourceFactory factory : factories)
                {
                List<MuseResource> created_resources = factory.createResources(origin, locator);
                if (created_resources.size() == 1)
                    {
                    MuseResource resource = created_resources.get(0);
                    if (resource.getMetadata().getId() == null)
                        resource.getMetadata().setId(origin.suggestId());
                    resource.getMetadata().setOrigin(origin);
                    ResourceType type = resource_types.forObject(resource);
                    if (type == null)
                        LOG.warn("Cannot find ResourceType for class: " + resource.getClass().getName());
                    else
                        resource.getMetadata().setType(type);
                    LOG.info(String.format("Loaded resource %s from %s", resource.getMetadata().getId(), resource.getMetadata().getOrigin().getDescription()));
                    }
                else if (created_resources.size() > 1)
                    LOG.error("Multiple resources within a resource is not supported (yet). resource = " + origin.getDescription());
                resources.addAll(created_resources);
                }
            if (resources.size() == 0)
                LOG.info(factories.size() + " factories found, but no resources created for " + origin.getDescription());
            }
        return resources;
        }

    final static Logger LOG = LoggerFactory.getLogger(ResourceFactory.class);
    }



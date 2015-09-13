package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Simplest possible in-memory resource store. Completely unoptimized. find...() performance should be expected
 * to degrade along O(n) with size of the store.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InMemoryResourceStore implements ResourceStore
    {
    @Override
    public void addResource(MuseResource resource)
        {
        _resources.add(resource);
        }

    @Override
    public List<MuseResource> findResources(ResourceMetadata matcher)
        {
        List<MuseResource> resources = new ArrayList<>();
        for (MuseResource resource : _resources)
            {
            boolean match = true;
            for (String name : matcher.getAttributeNames())
                {
                Object attribute = matcher.getAttribute(name);
                if (attribute != null && !attribute.equals(resource.getMetadata().getAttribute(name)))
                    match = false;
                }
            if (match)
                resources.add(resource);
            }
        return resources;
        }

    public void loadResource(ResourceOrigin origin)
        {
        List<MuseResourceFactory> factories = _factory_locator.findFactories(MuseResourceFactory.class);
        for (MuseResourceFactory factory : factories)
            {
            try
                {
                List<MuseResource> resources = factory.createResources(origin, getClassLocator());
                _resources.addAll(resources)
                ;
                }
            catch (IOException e)
                {
                LOG.error("can't create this mock resource?", e);
                }
            }
        }

    @Override
    public ClassLoader getContextClassloader()
        {
        return getClass().getClassLoader();
        }

    @Override
    public ClassLocator getClassLocator()
        {
        return _class_locator;
        }

    protected void setClassLocator(ClassLocator locator)
        {
        _class_locator = locator;
        _factory_locator = new FactoryLocator(locator);
        }

    protected FactoryLocator getFactoryLocator()
        {
        return _factory_locator;
        }

    @Override
    public String saveResource(MuseResource resource)
        {
        return null;  // nothing to do for the in-memory store
        }

    private List<MuseResource> _resources = new ArrayList<>();
    private ClassLocator _class_locator = DefaultClassLocator.get();
    private FactoryLocator _factory_locator = new FactoryLocator(DefaultClassLocator.get());

    final static Logger LOG = LoggerFactory.getLogger(InMemoryResourceStore.class);
    }



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
        if (resource.getMetadata().getId() == null)
            resource.getMetadata().setId(UUID.randomUUID().toString());
        _resources.add(resource);
        }

    @Override
    public List<ResourceToken> findResources(ResourceMetadata matcher)
        {
        List<ResourceToken> resources = new ArrayList<>();
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
                resources.add(new InMemoryResourceToken(resource));
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
    public <T extends MuseResource> T getResource(ResourceToken<T> token)
        {
        for (MuseResource resource : _resources)
            if (resource.getMetadata().getId().equals(token.getMetadata().getId()))
                return (T) resource;
        return null;
        }

    @Override
    public <T extends MuseResource> List<T> getResources(List<ResourceToken<T>> tokens)
        {
        List<T> resources = new ArrayList<>();
        for (ResourceToken<T> token : tokens)
            resources.add(getResource(token));
        return resources;
        }

    @Override
    public List<MuseResource> getUntypedResources(List<ResourceToken> tokens)
        {
        List<MuseResource> resources = new ArrayList<>();
        for (ResourceToken token : tokens)
            resources.add(getResource(token));
        return resources;
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

    private class InMemoryResourceToken implements ResourceToken
        {
        public InMemoryResourceToken(MuseResource resource)
            {
            _resource = resource;
            }

        @Override
        public MuseResource getResource()
            {
            return _resource;
            }

        @Override
        public ResourceMetadata getMetadata()
            {
            return _resource.getMetadata();
            }

        @Override
        public boolean equals(Object obj)
            {
            if (!(obj instanceof InMemoryResourceToken))
                return false;
            InMemoryResourceToken other = (InMemoryResourceToken) obj;
            return _resource.getMetadata().getId().equals(other.getMetadata().getId());
            }

        private MuseResource _resource;
        }
    }



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
    public ResourceToken addResource(MuseResource resource)
        {
        if (getResource(resource.getId()) != null)
            throw new IllegalArgumentException("Resource with already exists with the same ID: " + resource.getId());
        if (resource.getId() == null)
            resource.setId(UUID.randomUUID().toString());

        _resources.add(resource);
        InMemoryResourceToken token = new InMemoryResourceToken(resource);
        for (ProjectResourceListener listener : _listeners)
            listener.resourceAdded(token);
        return token;
        }

    @Override
    public boolean removeResource(ResourceToken token)
        {
        MuseResource resource = getResource(token.getId());
        if (resource == null)
            return false;
        _resources.remove(resource);
        for (ProjectResourceListener listener : _listeners)
            listener.resourceRemoved(token);
        return true;
        }

    public MuseResource getResource(String id)
        {
        for (MuseResource resource : _resources)
            if (resource.getId().equals(id))
                return resource;
        return null;
        }

    @Override
    public List<ResourceToken> findResources(ResourceAttributes attributes)
        {
        List<ResourceToken> matches = new ArrayList<>();
        for (MuseResource resource : _resources)
            {
            if (attributes._types.size() == 0 || attributes._types.contains(resource.getType()))
                {
                if (attributes._id == null || resource.getId().equals(attributes._id) )
                    matches.add(new InMemoryResourceToken(resource));
                }
            }
        return matches;
        }

    @Override
    public <T extends MuseResource> List<T> getResources(List<ResourceToken> tokens, Class<T> implementing_class)
        {
        List<T> resources = new ArrayList<>();
        for (ResourceToken token : tokens)
            {
            MuseResource resource = getResource(token.getId());
            if (implementing_class.isInstance(resource))
                resources.add((T) resource);
            else
                resources.add(null);
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
            if (resource.getId().equals(token.getId()))
                return (T) resource;
        return null;
        }

    @Override
    public List<MuseResource> getResources(List<ResourceToken> tokens)
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

    void setClassLocator(ClassLocator locator)
        {
        _class_locator = locator;
        _factory_locator = new FactoryLocator(locator);
        }

    FactoryLocator getFactoryLocator()
        {
        return _factory_locator;
        }

    @Override
    public String saveResource(MuseResource resource)
        {
        return null;  // nothing to do for the in-memory store
        }

    @Override
    public boolean addResourceListener(ProjectResourceListener listener)
        {
        if (_listeners.contains(listener))
            return false;
        _listeners.add(listener);
        return true;
        }

    @Override
    public boolean removeResourceListener(ProjectResourceListener listener)
        {
        return _listeners.remove(listener);
        }

    private List<MuseResource> _resources = new ArrayList<>();
    private ClassLocator _class_locator = DefaultClassLocator.get();
    private FactoryLocator _factory_locator = new FactoryLocator(DefaultClassLocator.get());

    private transient List<ProjectResourceListener> _listeners = new ArrayList<>();

    private final static Logger LOG = LoggerFactory.getLogger(InMemoryResourceStore.class);
    }



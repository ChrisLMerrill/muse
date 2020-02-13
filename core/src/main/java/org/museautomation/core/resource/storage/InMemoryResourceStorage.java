package org.museautomation.core.resource.storage;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.util.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * Simplest possible in-memory resource store. Completely unoptimized. find...() performance should be expected
 * to degrade along O(n) with size of the store.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InMemoryResourceStorage implements ResourceStorage
    {
    public InMemoryResourceStorage()
        {
        }

    @SuppressWarnings("unused")  // public API for testing
    public InMemoryResourceStorage(ClassLocator class_locator)
        {
        _class_locator = class_locator;
        }

    @Override
    public ResourceToken addResource(MuseResource resource) throws IOException
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
    public <T extends MuseResource> T getResource(String id, Class<T> implementing_class)
        {
        MuseResource resource = getResource(id);
        if (resource == null)
            return null;
        if (implementing_class.isInstance(resource))
            return (T) resource;
        return null;
        }

    @Override
    public List<ResourceToken> findResources(ResourceQueryParameters attributes)
        {
        List<ResourceToken> matches = new ArrayList<>();
        for (MuseResource resource : _resources)
            {
            if (attributes.getTypes().size() == 0 || attributes.getTypes().contains(resource.getType()) || (resource.getType().isSubtype() && attributes.getTypes().contains(((ResourceSubtype)resource.getType()).getParentType())))
                {
                if (attributes.getId() == null || resource.getId().equals(attributes.getId()) )
                    matches.add(new InMemoryResourceToken(resource));
                }
            }
        return matches;
        }

    @Override
    public ResourceToken findResource(String id)
        {
        List<ResourceToken> tokens = findResources(new ResourceQueryParameters(id));
        if (tokens.size() == 0)
            return null;
        else if (tokens.size() == 1)
            return tokens.get(0);
        else
            throw new RuntimeException(String.format("The found two resources for id %s. This should never happen.", id));
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
        if (token == null)
            return null;
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

    private final static Logger LOG = LoggerFactory.getLogger(InMemoryResourceStorage.class);
    }
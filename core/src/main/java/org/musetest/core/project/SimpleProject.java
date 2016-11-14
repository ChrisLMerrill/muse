package org.musetest.core.project;

import org.musetest.builtins.value.property.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.step.factory.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleProject implements MuseProject
    {
    public SimpleProject(ResourceStorage resources)
        {
        _resources = resources;
        }

    public SimpleProject(ResourceStorage resources, String name)
        {
        _resources = resources;
        _name = name;
        }

    public SimpleProject()
        {
        _resources = new InMemoryResourceStorage();
        }

    @Override
    public void open()
        {
        }

    @Override
    public String getName()
        {
        return _name;
        }

    @Override
    public MuseResource getResource(String id)
        {
        return _resources.getResource(id);
        }

    @Override
    public <T extends MuseResource> T getResource(String id, Class<T> implementing_class)
        {
        MuseResource resource = getResource(id);
        if (implementing_class.isInstance(resource))
            return (T) resource;
        return null;
        }

    @Override
    public List<ResourceToken> findResources(ResourceAttributes attributes)
        {
        return _resources.findResources(attributes);
        }

    @Override
    public ResourceToken findResource(String id)
        {
        List<ResourceToken> tokens = _resources.findResources(new ResourceAttributes(id));
        if (tokens.size() == 0)
            return null;
        else if (tokens.size() == 1)
            return tokens.get(0);
        else
            throw new RuntimeException(String.format("The resource store returned two resources for id %s. This should never happen.", id));
        }

    @Override
    public <T extends MuseResource> List<T> getResources(List<ResourceToken> tokens, Class<T> implementing_class)
        {
        return _resources.getResources(tokens, implementing_class);
        }

    public ResourceTypes getResourceTypes()
        {
        if (_resource_types == null)
            _resource_types = new ResourceTypes(getClassLocator());
        return _resource_types;
        }

    @Override
    public <T extends MuseResource> T getResource(ResourceToken<T> token)
        {
        if (token == null)
            return null;
        return _resources.getResource(token);
        }

    @Override
    public List<MuseResource> getResources(List<ResourceToken> tokens)
        {
        return _resources.getResources(tokens);
        }

    @Override
    public ResourceToken addResource(MuseResource resource)
        {
        return _resources.addResource(resource);
        }

    @Override
    public boolean removeResource(ResourceToken token)
        {
        return _resources.removeResource(token);
        }

    @Override
    public String saveResource(MuseResource resource)
        {
        return _resources.saveResource(resource);
        }

    @Override
    public ClassLoader getClassloader()
        {
        return _resources.getContextClassloader();
        }

    @Override
    public StepFactory getStepFactory()
        {
        if (_step_factory == null)
            _step_factory = new CompoundStepFactory(new ClasspathStepFactory(new TypeLocator(this)), new JavascriptStepFactory());
        return _step_factory;
        }

    @Override
    public ClassLocator getClassLocator()
        {
        return _resources.getClassLocator();
        }

    @Override
    public StepDescriptors getStepDescriptors()
        {
        if (_step_descriptors == null)
            _step_descriptors = new StepDescriptors(this);
        return _step_descriptors;
        }

    @Override
    public ValueSourceDescriptors getValueSourceDescriptors()
        {
        if (_source_descriptors == null)
            _source_descriptors = new ValueSourceDescriptors(this);
        return _source_descriptors;
        }

    @Override
    public ValueSourceStringExpressionSupporters getValueSourceStringExpressionSupporters()
        {
        if (_supporters == null)
            _supporters = new ValueSourceStringExpressionSupporters(this);
        return _supporters;
        }

    @Override
    public PropertyResolvers getPropertyResolvers()
        {
        if (_resolvers == null)
            _resolvers = new PropertyResolvers(this);
        return _resolvers;
        }

    @Override
    public SystemVariableProviders getSystemVariableProviders()
        {
        if (_sysvar_providers == null)
            _sysvar_providers = new SystemVariableProviders(this);
        return _sysvar_providers;
        }

    @Override
    public void setCommandLineOptions(Map<String, String> options)
        {
        if (_command_line_options != null)
            throw new IllegalArgumentException("Command-line options may only be set once for a project");
        _command_line_options = options;
        }

    @Override
    public Map<String, String> getCommandLineOptions()
        {
        if (_command_line_options == null)
            return Collections.EMPTY_MAP;
        else
            return Collections.unmodifiableMap(_command_line_options);
        }

    @Override
    public boolean addResourceListener(ProjectResourceListener listener)
        {
        return _resources.addResourceListener(listener);
        }

    @Override
    public boolean removeResourceListener(ProjectResourceListener listener)
        {
        return _resources.removeResourceListener(listener);
        }

    private ResourceStorage _resources;
    private StepFactory _step_factory;
    private StepDescriptors _step_descriptors;
    private ValueSourceDescriptors _source_descriptors;
    private ValueSourceStringExpressionSupporters _supporters;
    private PropertyResolvers _resolvers;
    private SystemVariableProviders _sysvar_providers;
    private ResourceTypes _resource_types;
    private Map<String, String> _command_line_options;
    private String _name = "unnamed project";
    }



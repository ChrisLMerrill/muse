package org.musetest.core.project;

import org.musetest.builtins.value.property.*;
import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.step.factory.*;
import org.musetest.core.test.plugins.*;
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
    public ResourceStorage getResourceStorage()
        {
        return _resources;
        }

    public ResourceTypes getResourceTypes()
        {
        if (_resource_types == null)
            _resource_types = new ResourceTypes(getClassLocator());
        return _resource_types;
        }

    @Override
    public TestPluginTypes getTestPluginTypes()
	    {
	    if (_ci_types == null)
		    _ci_types = new TestPluginTypes(getClassLocator());
        return _ci_types;
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
    private TestPluginTypes _ci_types;
    private Map<String, String> _command_line_options;
    private String _name = "unnamed project";
    }



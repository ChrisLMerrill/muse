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
import org.slf4j.*;

import java.util.*;
import java.util.stream.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleProject implements MuseProject
    {
    public SimpleProject(ResourceStore resources)
        {
        _resources = resources;
        }

    public SimpleProject(ResourceStore resources, String name)
        {
        _resources = resources;
        _name = name;
        }

    public SimpleProject()
        {
        _resources = new InMemoryResourceStore();
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
    public List<MuseResource> findResources(ResourceMetadata filter)
        {
        return _resources.findResources(filter);
        }

    @Override
    public <T extends MuseResource> List<T> findResources(ResourceMetadata filter, Class type)
        {
        List<MuseResource> resources = findResources(filter);
        List<T> found = new ArrayList<>();
        for (MuseResource resource : resources)
            if (type.isInstance(resource))
                found.add((T)resource);
        return found;
        }

    @Override
    public MuseResource findResource(ResourceMetadata filter)
        {
        List<MuseResource> resources = _resources.findResources(filter);
        if (resources.size() == 0)
            return null;
        else if (resources.size() > 1)
            LOG.warn("Multiple matches found for filter: " + filter.toString());
        return resources.get(0);
        }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T findResource(String id, Class<T> interface_class)
        {
        ResourceMetadata filter = new ResourceMetadata();
        filter.setId(id);
        filter.setType(new ResourceTypes(getClassLocator()).forImplementationInterface(interface_class));
        List<MuseResource> resources = _resources.findResources(filter);
        List<MuseResource> matching_resources = resources.stream().filter(interface_class::isInstance).collect(Collectors.toList());
        if (matching_resources.size() == 0)
            return null;
        else if (matching_resources.size() > 1)
            LOG.warn("Multiple matches found for id=" + id + ", interface=" + interface_class.getSimpleName());
        return (T) matching_resources.get(0);
        }

    @Override
    public void addResource(MuseResource resource)
        {
        _resources.addResource(resource);
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

    private ResourceStore _resources;
    private StepFactory _step_factory;
    private StepDescriptors _step_descriptors;
    private ValueSourceDescriptors _source_descriptors;
    private ValueSourceStringExpressionSupporters _supporters;
    private PropertyResolvers _resolvers;
    private SystemVariableProviders _sysvar_providers;
    private Map<String, String> _command_line_options;
    private String _name = "unnamed project";

    private final static Logger LOG = LoggerFactory.getLogger(SimpleProject.class);
    }



package org.musetest.core.project;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.step.factory.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.core.variables.*;
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

    public SimpleProject()
        {
        _resources = new InMemoryResourceStore();
        }

    @Override
    public void open()
        {
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
    public void initializeContext(MuseExecutionContext context)
        {
        List<VariableList> lists = findResources(new ResourceMetadata(new VariableList.VariableListType()), VariableList.class);
        for (VariableList list : lists)
            {
            for (String name : list.getVariables().keySet())
                {
                ValueSourceConfiguration config = list.getVariables().get(name);
                try
                    {
                    Object value = config.createSource(this).resolveValue(context);
                    context.setVariable(name, value, VariableScope.Execution);
                    }
                catch (Exception e)
                    {
                    LOG.error("This default variable cannot be initialized: " + name + ". Perhaps later, when deferred-evaluation is implemented.");
                    }
                }
            }
        }

    private ResourceStore _resources;
    private StepFactory _step_factory;
    private StepDescriptors _step_descriptors;
    private ValueSourceDescriptors _source_descriptors;
    private ValueSourceStringExpressionSupporters _supporters;

    private final static Logger LOG = LoggerFactory.getLogger(SimpleProject.class);
    }



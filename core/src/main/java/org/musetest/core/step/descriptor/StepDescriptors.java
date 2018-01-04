package org.musetest.core.step.descriptor;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.util.*;
import org.musetest.javascript.factory.*;
import org.slf4j.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * Locates a StepDescriptors based on what the caller has available. See the get() methods for available variations.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unchecked")
public class StepDescriptors
    {
    public StepDescriptors(MuseProject project)
        {
        _project = project;
        load();
        }

    public StepDescriptor get(StepConfiguration step)
        {
        return get(step.getType());
        }

    public StepDescriptor get(String type)
        {
        return get(type, false);
        }

    /**
     * @param allow_null Return null if no descriptor found (instead of an UnknownStepDescriptor).
     */
    public StepDescriptor get(String type, boolean allow_null)
        {
        StepDescriptor descriptor = _descriptors_by_type.get(type);
        if (descriptor != null)
            return descriptor;
        if (allow_null)
            return null;
        else
            return new UnknownStepDescriptor(type, _project);
        }

    public StepDescriptor get(Class<? extends MuseStep> step_implementation)
        {
        StepDescriptor descriptor = _descriptors_by_class.get(step_implementation);
        if (descriptor != null)
            return descriptor;
        descriptor = loadByClass(step_implementation);
        if (descriptor != null)
            {
            _descriptors_by_class.put(step_implementation, descriptor);
            _all_descriptors.add(descriptor);
            }
        return descriptor;
        }

    public Collection<StepDescriptor> findAll()
        {
        return _all_descriptors;
        }

    private void load()
        {
        List<Class<? extends MuseStep>> implementors = new TypeLocator(_project).getImplementors(MuseStep.class);
        for (Class step_class : implementors)
            {
            StepDescriptor descriptor = loadByClass(step_class);
            _descriptors_by_class.put(step_class, descriptor);
            _descriptors_by_type.put(descriptor.getType(), descriptor);
            _all_descriptors.add(descriptor);
            }

        // find descriptors for scripted steps
        List<ResourceToken> tokens = _project.getResourceStorage().findResources(new ResourceQueryParameters(new JavascriptStepResource.JavascriptStepResourceType()));
        List<MuseResource> scripted_steps = _project.getResourceStorage().getResources(tokens);
        for (MuseResource step : scripted_steps)
             {
            JavascriptStepResource step_resource = (JavascriptStepResource) step;
            StepDescriptor descriptor = step_resource.getStepDescriptor(_project);
            _descriptors_by_type.put(descriptor.getType(), descriptor);
            _all_descriptors.add(descriptor);
            }
        }

    private StepDescriptor loadByClass(Class<? extends MuseStep> step_implementation)
        {
        MuseStepDescriptorImplementation descriptor_annotation = step_implementation.getAnnotation(MuseStepDescriptorImplementation.class);
        if (descriptor_annotation != null)
            {
            Class<? extends StepDescriptor> descriptor_class = descriptor_annotation.value();
            try
                {
                if (StepDescriptor.class.isAssignableFrom(descriptor_class))
                    {
                    Constructor<? extends StepDescriptor> constructor = descriptor_class.getConstructor(MuseProject.class);
                    return constructor.newInstance(_project);
                    }
                else
                    LOG.error(String.format("The specified class (%s) does not implement the required interface (%s)", descriptor_class.getSimpleName(), StepDescriptor.class.getSimpleName()));
                return descriptor_class.newInstance();
                }
            catch (Exception e)
                {
                LOG.error("Unable to create the StepDescriptor based on the " + MuseStepDescriptorImplementation.class.getSimpleName() + " annotation. Implementing class is: " + descriptor_annotation.value().getSimpleName(), e);
                }
            }
        return new AnnotatedStepDescriptor(step_implementation, _project);
        }



    public MuseProject getProject()
        {
        return _project;
        }

    private MuseProject _project;

    private Map<String, StepDescriptor> _descriptors_by_type = new HashMap<>();
    private Map<Class, StepDescriptor> _descriptors_by_class = new HashMap<>();
    private Set<StepDescriptor> _all_descriptors = new HashSet<>();

    private final static Logger LOG = LoggerFactory.getLogger(StepDescriptors.class);
    }



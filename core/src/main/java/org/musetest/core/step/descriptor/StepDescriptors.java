package org.musetest.core.step.descriptor;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
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
        }

    public StepDescriptor get(StepConfiguration step)
        {
        return get(step.getType());
        }

    public StepDescriptor get(String type)
        {
        try
            {
            Class step_class = new TypeLocator(_project).getClassForTypeId(type);
            if (step_class != null)
                return get(step_class);
            }
        catch (Exception e)
            {
            // handle below
            }
        return new UnknownStepDescriptor(type, _project);
        }

    public StepDescriptor get(Class<? extends MuseStep> step_implementation)
        {
        MuseStepDescriptorImplementation descriptor_annotation = step_implementation.getAnnotation(MuseStepDescriptorImplementation.class);
        if (descriptor_annotation != null)
            {
            Class<? extends StepDescriptor> descriptor_class = descriptor_annotation.value();
            if (descriptor_class != null)
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

    public List<StepDescriptor> findAll()
        {
        List<StepDescriptor> descriptors = new ArrayList<>();

        // find all descriptors for Java implementations
        List<Class<? extends MuseStep>> implementors = new TypeLocator(_project).getImplementors(MuseStep.class);
        for (Class step_class : implementors)
            descriptors.add(_project.getStepDescriptors().get(step_class));

        // find descriptors for scripted steps
        List<MuseResource> scripted_steps = _project.findResources(new ResourceMetadata(ResourceTypes.jsStep));
        for (MuseResource step : scripted_steps)
            {
            JavascriptStepResource step_resource = (JavascriptStepResource) step;
            descriptors.add(step_resource.getStepDescriptor());
            }

        return descriptors;
        }

    public MuseProject getProject()
        {
        return _project;
        }

    private MuseProject _project;

    final static Logger LOG = LoggerFactory.getLogger(StepDescriptors.class);
    }



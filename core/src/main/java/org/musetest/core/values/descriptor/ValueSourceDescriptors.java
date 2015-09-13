package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.slf4j.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceDescriptors
    {
    public ValueSourceDescriptors(MuseProject project)
        {
        _project = project;
        }

    public ValueSourceDescriptor get(ValueSourceConfiguration source)
        {
        return get(source.getType());
        }

    public ValueSourceDescriptor get(String type_id)
        {
        for (Class implementing_class : _project.getClassLocator().getImplementors(MuseValueSource.class))
            {
            MuseTypeId type_annotation = (MuseTypeId) implementing_class.getAnnotation(MuseTypeId.class);
            if (type_annotation != null && type_id.equals(type_annotation.value()))
                return get(implementing_class);
            }
        return new UnknownValueSourceDescriptor(type_id, _project);
        }

    public ValueSourceDescriptor get(Class<? extends MuseValueSource> step_class)
        {
        String type_id = step_class.getAnnotation(MuseTypeId.class).value();

        try
            {
            MuseSourceDescriptorImplementation descriptor_annotation = step_class.getAnnotation(MuseSourceDescriptorImplementation.class);
            if (descriptor_annotation != null)
                {
                try
                    {
                    Class descriptor_class = descriptor_annotation.value();
                    if (ValueSourceDescriptor.class.isAssignableFrom(descriptor_class))
                        {
                        Constructor<ValueSourceDescriptor> constructor = descriptor_class.getConstructor(MuseProject.class);
                        return constructor.newInstance(_project);
                        }
                    else
                        LOG.error(String.format("The specified class (%s) does not implement the required interface (%s)", descriptor_class.getSimpleName(), ValueSourceDescriptor.class.getSimpleName()));
                    }
                catch (Exception e)
                    {
                    LOG.error("Unable to create the ValueSourceDescriptor based on the " + MuseSourceDescriptorImplementation.class.getSimpleName() + " annotation. Implementing class is: " + descriptor_annotation.value().getSimpleName(), e);
                    }
                }
            return new AnnotatedValueSourceDescriptor(type_id, step_class, _project);
            }
        catch (Exception e)
            {
            return new UnknownValueSourceDescriptor(type_id, _project);
            }
        }

    public List<ValueSourceDescriptor> findAll()
        {
        List<ValueSourceDescriptor> descriptors = new ArrayList<>();
        List<Class<? extends MuseValueSource>> implementors = new TypeLocator(_project).getImplementors(MuseValueSource.class);
        for (Class<? extends MuseValueSource> source_class : implementors)
            descriptors.add(_project.getValueSourceDescriptors().get(source_class));
        return descriptors;
        }

    private MuseProject _project;

    private static Logger LOG = LoggerFactory.getLogger(ValueSourceDescriptors.class);
    }



package org.musetest.core.values.descriptor;

import org.musetest.core.*;
import org.musetest.core.step.descriptor.*;
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
        load();
        }

    public ValueSourceDescriptor get(ValueSourceConfiguration source)
        {
        return get(source.getType());
        }

    public ValueSourceDescriptor get(String type_id)
        {
        return get(type_id, false);
        }

    /**
     * @param allow_null Return null if no descriptor found (instead of an UnknownValueSourceDescriptor).
     */
    public ValueSourceDescriptor get(String type, boolean allow_null)
        {
        ValueSourceDescriptor descriptor = _descriptors_by_type.get(type);
        if (descriptor != null)
            return descriptor;
        if (allow_null)
            return null;
        else
            return new UnknownValueSourceDescriptor(type, _project);
        }

    public ValueSourceDescriptor get(Class<? extends MuseValueSource> source_implementation)
        {
        ValueSourceDescriptor descriptor = _descriptors_by_class.get(source_implementation);
        if (descriptor != null)
            return descriptor;
        descriptor = loadByClass(source_implementation);
        if (descriptor != null)
            {
            _descriptors_by_class.put(source_implementation, descriptor);
            _all_descriptors.add(descriptor);
            }
        return descriptor;
        }

    public List<ValueSourceDescriptor> findAll()
        {
        return new ArrayList<>(_all_descriptors);  // TODO change signature to Set
        }

    private void load()
        {
        List<Class<? extends MuseValueSource>> implementors = new TypeLocator(_project).getImplementors(MuseValueSource.class);
        for (Class valuesource_class : implementors)
            {
            ValueSourceDescriptor descriptor = loadByClass(valuesource_class);
            _descriptors_by_class.put(valuesource_class, descriptor);
            _descriptors_by_type.put(descriptor.getType(), descriptor);
            _all_descriptors.add(descriptor);
            }
        }

    private ValueSourceDescriptor loadByClass(Class<? extends MuseValueSource> valuesource_implementation)
        {
        MuseTypeId type_annotation = valuesource_implementation.getAnnotation(MuseTypeId.class);
        MuseSourceDescriptorImplementation descriptor_annotation = valuesource_implementation.getAnnotation(MuseSourceDescriptorImplementation.class);
        if (descriptor_annotation != null)
            {
            Class<? extends ValueSourceDescriptor> descriptor_class = descriptor_annotation.value();
            try
                {
                if (ValueSourceDescriptor.class.isAssignableFrom(descriptor_class))
                    {
                    Constructor<? extends ValueSourceDescriptor> constructor = descriptor_class.getConstructor(MuseProject.class);
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
        return new AnnotatedValueSourceDescriptor(type_annotation.value(), valuesource_implementation, _project);
        }

    private MuseProject _project;
    private Map<String, ValueSourceDescriptor> _descriptors_by_type = new HashMap<>();
    private Map<Class, ValueSourceDescriptor> _descriptors_by_class = new HashMap<>();
    private Set<ValueSourceDescriptor> _all_descriptors = new HashSet<>();

    private static Logger LOG = LoggerFactory.getLogger(ValueSourceDescriptors.class);
    }
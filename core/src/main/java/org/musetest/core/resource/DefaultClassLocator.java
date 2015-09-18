package org.musetest.core.resource;

import org.musetest.core.*;
import org.reflections.*;
import org.slf4j.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unchecked")
public class DefaultClassLocator implements ClassLocator
    {
    @Override
    public Map<String, Class> getAnnotationValueToClassMap(Class annotation_class)
        {
        Map<String, Class> map = _annotation_value_to_class_map.get(annotation_class);
        if (map == null)
            {
            map = new HashMap<>();
            _annotation_value_to_class_map.put(annotation_class, map);

            Set<Class<?>> classes = _reflections.getTypesAnnotatedWith(annotation_class);
            for (Class the_class : classes)
                {
                Annotation annotation = the_class.getAnnotation(MuseTypeId.class);
                if (annotation == null)
                    {
                    LOG.error("Class is not annotated with @MuseTypeId(\"your-custom-type-id\"). Ignoring: " + the_class.getName());
                    continue;
                    }

                String value = ((MuseTypeId) annotation).value();
                if (map.get(value) == null)
                    {
                    map.put(value, the_class);
                    LOG.info(String.format("Registering annotation value %s=%s (for class %s)", annotation_class.getSimpleName(), value, the_class.getSimpleName()));
                    }
                else
                    LOG.error(String.format("Found duplicate annotation values %s=%s (for class %s and %s)", annotation_class.getSimpleName(), value, the_class.getSimpleName(), map.get(value).getSimpleName()));
                }
            }

        return map;
        }

    @Override
    public <T> List<T> getInstances(Class T)
        {
        List<Class> implementors = getImplementors(T);

        List<T> instances = new ArrayList<>();
        for (Class implementor : implementors)
            {
            try
                {
                if (!Modifier.isAbstract(implementor.getModifiers()))
                    {
                    Object instance = implementor.newInstance();
                    instances.add((T) instance);
                    }
                }
            catch (Exception e)
                {
                LOG.warn("Unable to instantiate class " + implementor.getSimpleName() + ". Does it have a public no-arg constructor?");
                }
            }
        return instances;
        }

    public <T> List<Class> getImplementors(Class T)
        {
        List<Class> implementors = _implementor_lists.get(T);
        if (implementors == null)
            {
            Set<Class<? extends T>> classes = _reflections.getSubTypesOf(T);
            implementors = new ArrayList<>(classes);
            _implementor_lists.put(T, implementors);
            }
        return implementors;
        }

    private Map<Class, Map<String, Class>> _annotation_value_to_class_map = new HashMap<>();
    private Map<Class, List<Class>> _implementor_lists = new HashMap<>();
    protected Reflections _reflections = DEFAULT_REFLECTIONS;

    public static ClassLocator get()
        {
        return DEFAULT;
        }

    private final static Reflections DEFAULT_REFLECTIONS = new Reflections("org.musetest");
    private final static ClassLocator DEFAULT = new DefaultClassLocator();

    final static Logger LOG = LoggerFactory.getLogger(DefaultClassLocator.class);
    }



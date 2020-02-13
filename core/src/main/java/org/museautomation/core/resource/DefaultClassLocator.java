package org.museautomation.core.resource;

import org.museautomation.builtins.value.property.*;
import org.museautomation.builtins.value.sysvar.*;
import org.museautomation.core.*;
import org.reflections.*;
import org.slf4j.*;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.atomic.*;

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

            _annotation_value_to_class_map.put(annotation_class, map);
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
            final AtomicReference<Set<Class<? extends T>>> classes = new AtomicReference<>();
            runOnClassloader(() -> classes.set(_reflections.getSubTypesOf(T)));
            implementors = new ArrayList<>(classes.get());
            _implementor_lists.put(T, implementors);
            }
        return implementors;
        }

    private void runOnClassloader(Runnable runner)
        {
        Thread thread = new Thread(runner);
        thread.setContextClassLoader(_classloader);
        thread.start();
        try
            {
            thread.join();
            }
        catch (InterruptedException e)
            {
            // ok
            }
        }

    private Map<Class, Map<String, Class>> _annotation_value_to_class_map = new HashMap<>();
    private Map<Class, List<Class>> _implementor_lists = new HashMap<>();
    protected Reflections _reflections = DEFAULT_REFLECTIONS;
    protected ClassLoader _classloader = getClass().getClassLoader();

    public static ClassLocator get()
        {
        // Not happy with this hack.
        // These may not be retrieved before starting a test, since they are not needed for rendering the test. So we have to force lookups before a test starts.
        DEFAULT.getImplementors(SystemVariableProvider.class);
        DEFAULT.getImplementors(PropertyResolver.class);

        return DEFAULT;
        }

    private final static Reflections DEFAULT_REFLECTIONS = new Reflections("org.musetest");
    private final static ClassLocator DEFAULT = new DefaultClassLocator();

    private final static Logger LOG = LoggerFactory.getLogger(DefaultClassLocator.class);
    }
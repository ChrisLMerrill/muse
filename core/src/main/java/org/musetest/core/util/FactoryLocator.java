package org.musetest.core.util;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.reflections.*;
import org.slf4j.*;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FactoryLocator
    {
    public FactoryLocator(ClassLocator locator)
        {
        if (locator == null)
            _locator = DefaultClassLocator.get();
        else
            _locator = locator;
        }

    /**
     * Finds all classes that implement the provided interface (must extend MuseDynamicLoadable)
     * and returns an instance of each.
     *
     * WARNING: The class lookups are cached to improve performance and thus will only be performed
     *          once per JVM instance.
     */
    public static <T> List<T> findFactories(Class T, Reflections reflections)
        {
        ClassLocator locator = DefaultClassLocator.get();
        return locator.getInstances(T);
        }

    public <T> List<T> findFactories(Class T)
        {
// TODO we can cache the results, since they're just factories...
        return _locator.getInstances(T);
        }

    private final ClassLocator _locator;

    private static ThreadLocal<Set<Class<? extends MuseDynamicLoadable>>> LOADABLES = new ThreadLocal<>();

    final static Logger LOG = LoggerFactory.getLogger(FactoryLocator.class);
    }



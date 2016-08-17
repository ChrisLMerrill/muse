package org.musetest.core.util;

import org.musetest.core.resource.*;
import org.reflections.*;

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
     *
     * @param T The type of factory to find
     * @param <T> They type of factory
     * @param reflections The Reflections object to use in the search
     *
     * @return A list of factories classes of type T.
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
    }



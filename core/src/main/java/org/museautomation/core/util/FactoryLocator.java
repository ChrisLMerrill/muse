package org.museautomation.core.util;

import org.museautomation.core.resource.*;

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

    public <T> List<T> findFactories(Class T)
        {
// TODO we can cache the results, since they're just factories...
        return _locator.getInstances(T);
        }

    private final ClassLocator _locator;
    }



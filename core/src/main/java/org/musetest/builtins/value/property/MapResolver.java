package org.musetest.builtins.value.property;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MapResolver implements PropertyResolver
    {
    @Override
    public boolean canResolve(Object target, String name)
        {
        return target instanceof Map;
        }

    @Override
    public Object resolve(Object target, String name) throws MuseExecutionError
        {
        return ((Map)target).get(name);
        }
    }



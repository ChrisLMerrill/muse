package org.museautomation.builtins.value.property;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * Resolves properties on a Map (or Dictionary).
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MapResolver implements PropertyResolver
    {
    @Override
    public boolean canResolve(Object target, String name)
        {
        return target instanceof Map || target instanceof Dictionary;
        }

    @Override
    public Object resolve(Object target, String name) throws MuseExecutionError
        {
        if (target instanceof Map)
            return ((Map)target).get(name);
        else if (target instanceof Dictionary)
            return ((Dictionary)target).get(name);
        else
            throw new ValueSourceResolutionError("MapResolver unable to resolve target of type " + target.getClass().getSimpleName());
        }
    }



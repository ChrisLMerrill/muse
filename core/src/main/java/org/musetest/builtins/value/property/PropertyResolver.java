package org.musetest.builtins.value.property;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface PropertyResolver
    {
    boolean canResolve(Object target, String name);
    Object resolve(Object target, String name) throws MuseExecutionError;
    }


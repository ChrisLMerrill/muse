package org.musetest.builtins.value.property;

import org.musetest.core.*;

/**
 * Resolves a named property on a value (or object). It is used by the PropertySource.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface PropertyResolver
    {
    boolean canResolve(Object target, String name);
    Object resolve(Object target, String name) throws MuseExecutionError;
    }


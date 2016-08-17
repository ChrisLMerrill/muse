package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SystemVariableProvider
    {
    boolean provides(String name);
    Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError;
    }

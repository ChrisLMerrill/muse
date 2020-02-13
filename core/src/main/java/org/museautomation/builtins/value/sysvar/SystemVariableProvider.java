package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

/**
 * Provides system variable evaluation within a MuseExecutionContext
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface SystemVariableProvider
    {
    boolean provides(String name);
    Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError;
    }


package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;
import org.museautomation.core.util.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // discovered via reflection
public class OperatingSystemVariableProvider implements  SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return VARNAME.equals(name);
        }

    @Override
    public OperatingSystem resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return OperatingSystem.get();
        }

    private final static String VARNAME = "opsys";
    }



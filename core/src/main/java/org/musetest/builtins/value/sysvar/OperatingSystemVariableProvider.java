package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

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



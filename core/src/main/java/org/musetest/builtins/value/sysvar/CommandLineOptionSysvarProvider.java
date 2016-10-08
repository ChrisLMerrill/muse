package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CommandLineOptionSysvarProvider implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return VARNAME1.equals(name) || VARNAME2.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        if (provides(name))
            return context.getProject().getCommandLineOptions();
        return null;
        }

    private final static String VARNAME1 = "commandlineoption";
    private final static String VARNAME2 = "clo";
    }



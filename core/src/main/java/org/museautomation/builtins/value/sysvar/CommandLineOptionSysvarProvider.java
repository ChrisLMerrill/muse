package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;

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
    public Object resolve(String name, MuseExecutionContext context)
        {
        if (provides(name))
            return context.getProject().getCommandLineOptions();
        return null;
        }

    private final static String VARNAME1 = "commandlineoption";
    private final static String VARNAME2 = "clo";
    }



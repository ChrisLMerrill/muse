package org.museautomation.core.test.plugins;

import org.museautomation.builtins.value.sysvar.*;
import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectVarsInitializerSysvarProvider implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return SYSVAR_NAME.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context)
        {
        if (provides(name))
            {
            Object value = context.getVariable(VARIABLE_LIST_ID_VARNAME);
            if (value != null)
                return value.toString();
            }
        return null;
        }

    public static final String SYSVAR_NAME = "varlistid";
    public static final String VARIABLE_LIST_ID_VARNAME = "__varlistid_init__";
    }



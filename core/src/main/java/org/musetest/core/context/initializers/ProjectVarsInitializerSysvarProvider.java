package org.musetest.core.context.initializers;

import org.musetest.builtins.value.sysvar.*;
import org.musetest.core.*;
import org.musetest.core.values.*;

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
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
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



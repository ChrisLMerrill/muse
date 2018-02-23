package org.musetest.builtins.value.sysvar;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExecutionIdProvider implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return VARNAME.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context)
        {
        if (provides(name))
            return context.getVariable(BaseExecutionContext.EXECUTION_ID_VARNAME, VariableScope.Execution);
        return null;
        }

    public final static String VARNAME = "ExecutionId";
    }



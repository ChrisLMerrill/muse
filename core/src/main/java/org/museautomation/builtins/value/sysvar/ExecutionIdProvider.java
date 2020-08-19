package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.variables.*;

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
            if (context instanceof TaskExecutionContext)
                return ((TaskExecutionContext) context).getTaskExecutionId();
            else
                return context.getVariable(BaseExecutionContext.EXECUTION_ID_VARNAME, VariableQueryScope.Project);
        return null;
        }

    public final static String VARNAME = "ExecutionId";
    }

package org.museautomation.builtins.value.sysvar;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskVariableProvider implements SystemVariableProvider
    {
    @Override
    public boolean provides(String name)
        {
        return SYSVAR_NAME.equals(name);
        }

    @Override
    public Object resolve(String name, MuseExecutionContext context) throws ValueSourceResolutionError
        {
        if (SYSVAR_NAME.equals(name))
            {
            TaskExecutionContext task_context = MuseExecutionContext.findAncestor(context, TaskExecutionContext.class);
            if (task_context == null)
	            throw new ValueSourceResolutionError("Cannot get the task variable - not executed within the context of a task.");
            return new TaskVariableProxy(task_context.getTask());
            }

        throw new ValueSourceResolutionError("Cannot provide this value. Did you check provides() first?");
        }

    public final static String SYSVAR_NAME = "task";

    /**
     * Provides controlled access to the task from a context SystemVariable.
     */
    @SuppressWarnings("WeakerAccess")  // can be accessed via reflection
    static public class TaskVariableProxy
        {
        public TaskVariableProxy(MuseTask task)
            {
            _task = task;
            }

        public Set<String> getTags()
            {
            return _task.getTags();
            }

        public String getId()
            {
            return _task.getId();
            }

        private MuseTask _task;
        }
    }



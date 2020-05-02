package org.museautomation.core.task.input;

import org.museautomation.core.task.*;

import java.util.*;

/**
 * An implementation of TaskInputProvider that only resolves a single input
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class SingleTaskInputProvider implements TaskInputProvider
    {
    @Override
    public List<ResolvedTaskInput> resolveInputs(TaskInputResolutionResults resolved, UnresovledTaskInputs inputs)
        {
        for (TaskInput input : inputs.list())
            {
            Object value = resolveInput(resolved, input);
            if (value != null)
                return Collections.singletonList(new ResolvedTaskInput(input.getName(), value, new ResolvedInputSource.InputProviderSource(this)));
            }
        return Collections.emptyList();
        }

    /**
     * This method is called with each unresolved input until it returns non-null. The result will be
     * returned as the only ResolvedTaskInput from this provider
     */
    public abstract Object resolveInput(TaskInputResolutionResults resolved, TaskInput input);
    }
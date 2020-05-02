package org.museautomation.core.task.input;

import org.museautomation.core.task.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UnresolvedTaskInputs
    {
    public UnresolvedTaskInputs(List<TaskInput> inputs)
        {
        _inputs = inputs;
        }

    public boolean hasInput(String name)
        {
        return getInput(name) != null;
        }

    public TaskInput getInput(String name)
        {
        for (TaskInput input : _inputs)
            if (name.equals(input.getName()))
                return input;
        return null;
        }

    public List<TaskInput> list()
        {
        return _inputs;
        }

    private final List<TaskInput> _inputs;
    }
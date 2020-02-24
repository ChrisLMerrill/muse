package org.museautomation.core.task;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInputSet
    {
    public Collection<TaskInput> all()
        {
        return _inputs.values();
        }

    /**
     * Intended only de/serialization support
     */
    public Set<TaskInput> getList()
        {
        return new HashSet<>(_inputs.values());
        }

    /**
     * Intended only de/serialization support
     */
    public void setList(Set<TaskInput> inputs)
        {
        _inputs.clear();
        for (TaskInput input : inputs)
            _inputs.put(input.getName(), input);
        }

    private Map<String, TaskInput> _inputs = new HashMap<>();
    }
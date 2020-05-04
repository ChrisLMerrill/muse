package org.museautomation.core.task;

import com.fasterxml.jackson.annotation.*;

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
     * Intended only for de/serialization support
     */
    @Deprecated // not really, but this may to keep it from being used directly
    public Set<TaskInput> getList()
        {
        return new HashSet<>(_inputs.values());
        }

    /**
     * Intended only for de/serialization support
     */
    @Deprecated // not really, but this may to keep it from being used directly
    public void setList(Set<TaskInput> inputs)
        {
        _inputs.clear();
        for (TaskInput input : inputs)
            _inputs.put(input.getName(), input);
        }

    public void addInput(TaskInput input)
        {
        _inputs.put(input.getName(), input);
        }

    @JsonIgnore
    public Collection<TaskInput> getInputs()
        {
        return _inputs.values();
        }

    @JsonIgnore
    public Set<String> getInputNames()
        {
        return _inputs.keySet();
        }

    public TaskInput getInput(String name)
        {
        return _inputs.get(name);
        }

    private Map<String, TaskInput> _inputs = new LinkedHashMap<>();  // used instead of HashMap to preserve the order
    }
package org.museautomation.core.task;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskOutputSet
    {
    public Collection<TaskOutput> all()
        {
        return _outputs.values();
        }

    /**
     * Intended only de/serialization support
     */
    @Deprecated // not really, but may keep it from being used directly
    public Set<TaskOutput> getList()
        {
        return new HashSet<>(_outputs.values());
        }

    /**
     * Intended only de/serialization support
     */
    @Deprecated // not really, but may keep it from being used directly
    public void setList(Set<TaskOutput> inputs)
        {
        _outputs.clear();
        for (TaskOutput input : inputs)
            _outputs.put(input.getName(), input);
        }

    public void addOutput(TaskOutput output)
        {
        _outputs.put(output.getName(), output);
        }

    @JsonIgnore
    public Collection<TaskOutput> getOutputs()
        {
        return _outputs.values();
        }

    private Map<String, TaskOutput> _outputs = new HashMap<>();
    }
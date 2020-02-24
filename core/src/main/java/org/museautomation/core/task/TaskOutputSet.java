package org.museautomation.core.task;

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
    public Set<TaskOutput> getList()
        {
        return new HashSet<>(_outputs.values());
        }

    /**
     * Intended only de/serialization support
     */
    public void setList(Set<TaskOutput> inputs)
        {
        _outputs.clear();
        for (TaskOutput input : inputs)
            _outputs.put(input.getName(), input);
        }

    private Map<String, TaskOutput> _outputs = new HashMap<>();
    }
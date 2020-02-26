package org.museautomation.core.task.state;

import java.util.*;

/**
 * Represents an instance of a StateDefinition that may be manually created, generated as the result of a Task completion,
 * or used as input to a Task.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 * @see StateDefinition
 * @see org.museautomation.core.MuseTask
 */
public class InterTaskState
    {
    public Map<String, Object> getValues()
        {
        return _values;
        }

    public void setValues(Map<String, Object> values)
        {
        _values = values;
        }

    public String getType()
        {
        return _type_id;
        }

    public void setType(String type_id)
        {
        _type_id = type_id;
        }

    public String getId()
        {
        return _id;
        }

    public void setId(String id)
        {
        _id = id;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!(obj instanceof InterTaskState))
            return false;
        InterTaskState other = (InterTaskState) obj;
        return _type_id.equals(other._type_id) && _id.equals(other._id);
        }

    private Map<String, Object> _values = new HashMap<>();
    private String _type_id;
    private String _id;
    }
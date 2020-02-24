package org.museautomation.core.task.state;

import org.museautomation.core.resource.types.*;

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

    public ResourceType getType()
        {
        return _type;
        }

    public void setType(ResourceType type)
        {
        _type = type;
        }

    public String getId()
        {
        return _id;
        }

    public void setId(String id)
        {
        _id = id;
        }

    private Map<String, Object> _values;
    private ResourceType _type;
    private String _id;
    }
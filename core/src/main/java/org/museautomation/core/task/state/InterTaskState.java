package org.museautomation.core.task.state;

import java.util.*;

/**
 * Represents a state in automation. States live outside of tasks...before, after and between. I.e. A state may be an
 * input to a task (the before) or the output of a task (the after). A state that is created by one task may become
 * the input to another task (the between).
 *
 * The InterTaskState contains the values described in the StateDefinition. As an example, a login task may generate a
 * LoggedInState, whereby the browser in question is now ready to perform tasks on that site. That LoggedInState then
 * serves as input to other tasks that require an authenticated session.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 * @see StateDefinition
 * @see StateTransitionConfiguration
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

    public String getStateDefinitionId()
        {
        return _type_id;
        }

    public void setStateDefinitionId(String type_id)
        {
        _type_id = type_id;
        }

    public String getId()
        {
        return _id;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!(obj instanceof InterTaskState))
            return false;
        InterTaskState other = (InterTaskState) obj;
        return _type_id.equals(other._type_id) && _id.equals(other._id);
        }

    public void setValue(String name, Object value)
        {
        _values.put(name, value);
        }

    private Map<String, Object> _values = new HashMap<>();
    private String _type_id;
    private String _id = UUID.randomUUID().toString();
    }
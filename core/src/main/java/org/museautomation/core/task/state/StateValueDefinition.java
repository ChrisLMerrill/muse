package org.museautomation.core.task.state;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.*;
import org.museautomation.core.valuetypes.*;

/**
 * Represents a specific value that will (may) be present within a state.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateValueDefinition
    {
    public StateValueDefinition()
        {
        }

    public StateValueDefinition(String name, MuseValueType type, boolean required)
        {
        _name = name;
        _type = type;
        _required = required;
        }

    public String getName()
        {
        return _name;
        }

    public void setName(String name)
        {
        _name = name;
        }

    @JsonIgnore
    public MuseValueType getType()
        {
        return _type;
        }

    @JsonIgnore
    public void setType(MuseValueType type)
        {
        _type = type;
        }

    public String getTypeId()
        {
        return _type.getId();
        }

    public void setTypeId(String id)
        {
        _type = MuseValueTypes.get().forTypeId(id);
        if (_type == null)
            throw new IllegalArgumentException(id + " is not a recognized ID for a MuseValueType");
        }

    public boolean isRequired()
        {
        return _required;
        }

    public void setRequired(boolean required)
        {
        _required = required;
        }

    private String _name;
    private MuseValueType _type;
    private boolean _required;
    }
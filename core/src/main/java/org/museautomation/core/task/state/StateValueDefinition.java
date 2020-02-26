package org.museautomation.core.task.state;

import org.museautomation.*;

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

    public MuseValueType getType()
        {
        return _type;
        }

    public void setType(MuseValueType type)
        {
        _type = type;
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
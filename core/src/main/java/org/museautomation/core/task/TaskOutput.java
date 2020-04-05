package org.museautomation.core.task;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.*;
import org.museautomation.core.valuetypes.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskOutput
    {
    public TaskOutput()
        {
        }

    public TaskOutput(String name, String type_id)
        {
        _name = name;
        _type_id = type_id;
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
        return MuseValueTypes.get().forTypeId(_type_id);
        }

    @JsonIgnore
    public void setType(MuseValueType type)
        {
        _type_id = type.getId();
        }

    public String getTypeId()
        {
        return _type_id;
        }

    public void setTypeId(String type_id)
        {
        _type_id = type_id;
        }

    private String _name;
    private String _type_id;
    }
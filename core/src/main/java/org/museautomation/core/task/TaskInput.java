package org.museautomation.core.task;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.*;
import org.museautomation.core.metadata.*;
import org.museautomation.core.util.*;
import org.museautomation.core.values.*;
import org.museautomation.core.valuetypes.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInput implements Taggable
    {
    // required for de/serialization
    @SuppressWarnings("unused")
    public TaskInput()
        {
        }

    public TaskInput(String name, String type_id, boolean required)
        {
        _name = name;
        _type_id = type_id;
        _required = required;
        }

    public TaskInput(String name, String type_id, ValueSourceConfiguration default_val)
        {
        _name = name;
        _type_id = type_id;
        _default = default_val;
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

    // required for de/serialization
    @SuppressWarnings("unused")
    public void setTypeId(String type_id)
        {
        _type_id = type_id;
        }

    public boolean isRequired()
        {
        return _required;
        }

    public void setRequired(boolean required)
        {
        _required = required;
        }

    public ValueSourceConfiguration getDefault()
        {
        return _default;
        }

    public void setDefault(ValueSourceConfiguration default_value)
        {
        _default = default_value;
        }


    /**
     * ONLY for serialization use!
     *
     * Exposes the underlying set for serialization purposes only. Use #tags for accessing the tags
     */
    public Set<String> getTags()
        {
        return _tags.getTags();
        }

    /**
     * ONLY for serialization use!
     *
     * Exposes the underlying set for serialization purposes only. Use #tags for accessing the tags
     */
    public void setTags(Set<String> tags)
        {
        _tags = new HashSetTagContainer(tags);
        }

    @Override
    public TagContainer tags()
        {
        return _tags;
        }

    public ContainsMetadata metadata()
	    {
	    return _metadata;
	    }

    /**
     * ONLY for serialization use!
     *
     * Exposes the underlying metadata map for serialization purposes only. Use #metadata for accessing the metadata
     */
    public Map<String, Object> getMetadata() { return _metadata.getMap(); }

    /**
     * ONLY for serialization use!
     *
     * Exposes the underlying metadata map for serialization purposes only. Use #metadata for accessing the metadata
     */
    public void setMetadata(Map<String, Object> map) { _metadata.setMap(map); }

    private String _name;
    private String _type_id;
    private boolean _required;
    private ValueSourceConfiguration _default;
    private final MetadataContainer _metadata = new MetadataContainer();
    private HashSetTagContainer _tags = new HashSetTagContainer();
    }
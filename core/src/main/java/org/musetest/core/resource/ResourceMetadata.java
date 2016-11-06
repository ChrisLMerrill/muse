package org.musetest.core.resource;

import org.musetest.core.*;
import org.musetest.core.resource.types.*;
import org.slf4j.*;

import java.util.*;

/**
 * Contains attributes about the resource that allow it to be identified/queried/located in a project
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unchecked")
public class ResourceMetadata
    {
    public ResourceMetadata()
        {
        }

    public ResourceMetadata(ResourceType type)
        {
        setType(type);
        }

    public void setId(String id)
        {
        setAttribute(ID_ATTRIBUTE, id);
        }
    public String getId()
        {
        return getAttribute(ID_ATTRIBUTE, String.class);
        }

    public void setOrigin(ResourceOrigin origin)
        {
        _origin = origin;
        }
    public ResourceOrigin getOrigin()
        {
        return _origin;
        }

    public void setType(ResourceType type)
        {
        setAttribute(TYPE_ATTRIBUTE, type);
        }
    public ResourceType getType()
        {
        return getAttribute(TYPE_ATTRIBUTE, ResourceType.class);
        }

    public <T> T getAttribute(String name, Class<T> type)
        {
        Object object = _attributes.get(name);
        if (object != null && type.isAssignableFrom(object.getClass()))
            return (T) object;
        return null;
        }
    public Object getAttribute(String name)
        {
        return _attributes.get(name);
        }
    public void setAttribute(String name, Object value)
        {
        _attributes.put(name, value);
        }

    public Set<String> getAttributeNames()
        {
        return _attributes.keySet();
        }

    public MuseResourceSaver getSaver()
        {
        return _saver;
        }

    public void setSaver(MuseResourceSaver saver)
        {
        _saver = saver;
        }

    public List<String> getTags()
        {
        return Collections.unmodifiableList(_tags);
        }

    @Override
    public String toString()
        {
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for (String key : _attributes.keySet())
            {
            if (!first)
                builder.append(", ");
            builder.append(key);
            builder.append("=");
            builder.append(_attributes.get(key));
            first = false;
            }
        return builder.toString();
        }

    public void addTag(String tag)
        {
        if (!_tags.contains(tag))
            _tags.add(tag);
        }

    private transient ResourceOrigin _origin;
    private transient MuseResourceSaver _saver;
    private Map<String, Object> _attributes = new HashMap<>();
    private List<String> _tags = new ArrayList<>();  // ideally, we'd like this to be a set. But we'd have to convert to a list for JSON serialization.
    public final static String ID_ATTRIBUTE = "id";
    public final static String TYPE_ATTRIBUTE = "type";
    }



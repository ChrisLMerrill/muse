package org.musetest.core.resource;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseMuseResource implements MuseResource
    {
    @Override
    public String getId()
        {
        return _id;
        }

    @Override
    public void setId(String id)
        {
        _id = id;
        }

    @Override
    public Set<String> getTags()
        {
        return Collections.unmodifiableSet(_tags);
        }

    @Override
    public void setTags(Set<String> tags)
        {
        _tags = tags;
        }

    @Override
    public boolean addTag(String tag)
        {
        if (_tags.contains(tag))
            return false;
        _tags.add(tag);
        return true;
        }

    @Override
    public boolean removeTag(String tag)
        {
        return _tags.remove(tag);
        }

    @Override
    public boolean hasTag(String tag)
        {
        return _tags.contains(tag);
        }

    private String _id;
    private Set<String> _tags = new HashSet<>();
    }



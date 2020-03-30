package org.museautomation.core.metadata;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class HashSetTagContainer implements TagContainer, Serializable
    {
    public HashSetTagContainer() { }

    public HashSetTagContainer(Set<String> tags)
        {
        _tags.addAll(tags);
        }

    @Override
    public Set<String> getTags()
        {
        return Collections.unmodifiableSet(_tags);
        }

    @Override
    public boolean addTag(String tag)
        {
        boolean added = _tags.add(tag);
        if (added)
            notifyListeners(tag, true);
        return added;
        }

    @Override
    public boolean removeTag(String tag)
        {
        boolean removed = _tags.remove(tag);
        if (removed)
            notifyListeners(tag, false);
        return removed;
        }

    @Override
    public boolean hasTag(String tag)
        {
        return _tags.contains(tag);
        }

    @Override
    public void addListener(TagChangeListener listener)
        {
        _listeners.add(listener);
        }

    @Override
    public void removeListener(TagChangeListener listener)
        {
        _listeners.remove(listener);
        }

    private void notifyListeners(String tag, boolean added)
        {
        for (TagChangeListener listener : _listeners)
            if (added)
                listener.tagAdded(tag);
            else
                listener.tagRemoved(tag);
        }

    private final Set<String> _tags = new HashSet<>();
    private final transient Set<TagChangeListener> _listeners = new HashSet<>();
    }
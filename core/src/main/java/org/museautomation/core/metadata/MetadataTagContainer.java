package org.museautomation.core.metadata;

import org.museautomation.core.util.*;

import java.io.*;
import java.util.*;

import static org.museautomation.core.step.StepConfiguration.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MetadataTagContainer implements TagContainer, Serializable
    {
    public MetadataTagContainer(MetadataContainer metadata)
        {
        _metadata = metadata;
        }

    @Override
    public Set<String> getTags()
        {
        return Collections.unmodifiableSet(internalGetTags());
        }

    public Set<String> internalGetTags()
        {
        Object meta = _metadata.getMetadataField(META_TAGS);
        if (meta instanceof Set)
            return (Set<String>) meta;
        else if (meta instanceof List)
            {
            HashSet<String> tags = new HashSet<>((List<String>) meta);
            _metadata.setMetadataField(META_TAGS, tags);
            return tags;
            }
        else
            return Collections.emptySet();
        }

    @Override
    public boolean addTag(String tag)
        {
        Set<String> tags = internalGetTags();
        if (tags.contains(tag))
            return false;
        if (tags.isEmpty())
            tags = new HashSet<>();
        tags.add(tag);
        _metadata.setMetadataField(META_TAGS, tags);
        notifyListeners(tag, true);
        return true;
        }

    @Override
    public boolean removeTag(String tag)
        {
        Set<String> tags = internalGetTags();
        if (tags.contains(tag))
            {
            tags.remove(tag);
            if (tags.isEmpty())
                _metadata.removeMetadataField(META_TAGS);
            else
                _metadata.setMetadataField(META_TAGS, tags);
            notifyListeners(tag, false);
            return true;
            }
        return false;
        }

    @Override
    public boolean hasTag(String tag)
        {
        Set<String> tags = internalGetTags();
        return tags.contains(tag);
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

    private final MetadataContainer _metadata;
    private final transient Set<TagChangeListener> _listeners = new HashSet<>();
    }
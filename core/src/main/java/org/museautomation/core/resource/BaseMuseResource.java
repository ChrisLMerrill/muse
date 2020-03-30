package org.museautomation.core.resource;

import org.museautomation.core.*;
import org.museautomation.core.metadata.*;
import org.museautomation.core.util.*;

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

    @Override
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

    private String _id;
    private MetadataContainer _metadata = new MetadataContainer();
    private HashSetTagContainer _tags = new HashSetTagContainer();
    }
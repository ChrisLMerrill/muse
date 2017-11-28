package org.musetest.core.resource;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.util.*;

/**
 * Contains basic information about a resource.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ResourceInfo extends Taggable
    {
    /**
     * The unique ID for this resource.
     *
     * Note that the ID is not serialized by Jackson. The ResourceStore is expected to add ID information
     * when storing the resource and infer it when restoring the resource. I.e. filesystem-based ResourceStore
     * would equate the ID to the filename.
     */
    @JsonIgnore
    String getId();

    /**
     * Set the ID for this resource. This should typically only be used when creating a new resoource.
     *
     * This method should NOT be used to change the ID of a resource that has already been read into a project,
     * because the project (and the underlying resource store) will have no idea it was changed. For resources
     * that are de-serialized from an existing project artifact, this should be set by the underlying ResourceStore.
     */
    @JsonIgnore
    void setId(String id);

    @JsonIgnore
    ResourceType getType();
    }


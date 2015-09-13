package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.resource.*;

/**
 * A MuseResource is the reference for any artifact that can exist at the top level of a project.
 *
 * If it is added directly to the project (i.e. not contained as a part of something else in the
 * project), the resources must be able to provide ResourceMetadata that describes the resource.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface MuseResource
    {
    @JsonIgnore
    ResourceMetadata getMetadata();
    }


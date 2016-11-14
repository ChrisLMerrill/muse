package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.resource.*;

import java.io.*;

/**
 * A MuseResource is the reference for any artifact that can exist at the top level of a project.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface MuseResource extends Serializable, ResourceInfo
    {
    @JsonIgnore
    @Deprecated
    ResourceMetadata getMetadata();
    }


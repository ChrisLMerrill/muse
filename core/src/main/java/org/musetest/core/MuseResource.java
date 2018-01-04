package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;

import java.io.*;

/**
 * A MuseResource is the reference for any first-class artifact that can exist in a MuseProject.
 * Note that only artifacts that can be stored independently are considered a MuseResource. Serializable
 * widgets that must be contained by something else are not a MuseResource.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface MuseResource extends Serializable, ResourceInfo
    {
    ContainsMetadata metadata();
    }


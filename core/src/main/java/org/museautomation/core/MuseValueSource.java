package org.museautomation.core;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface MuseValueSource
    {
    Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError;

    @JsonIgnore
    String getDescription();
    }


package org.musetest.core;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.context.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public interface MuseValueSource
    {
    Object resolveValue(StepExecutionContext context) throws StepConfigurationError;

    @JsonIgnore
    String getDescription();
    }


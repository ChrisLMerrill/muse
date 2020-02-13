package org.museautomation.core.step;

import org.museautomation.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionResult
    {
    StepExecutionStatus getStatus();
    String getDescription();
    MetadataContainer metadata();
    }
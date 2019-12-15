package org.musetest.core.step;

import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepExecutionResult
    {
    StepExecutionStatus getStatus();
    String getDescription();
    MetadataContainer metadata();
    }
package org.museautomation.core.steptest;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutionError extends MuseExecutionError
    {
    public StepExecutionError(String message)
        {
        super(message);
        }

    public StepExecutionError(String message, Throwable cause)
        {
        super(message, cause);
        }
    }



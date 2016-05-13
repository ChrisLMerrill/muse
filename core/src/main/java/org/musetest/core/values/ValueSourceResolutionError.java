package org.musetest.core.values;

import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceResolutionError extends StepExecutionError
    {
    public ValueSourceResolutionError(String message)
        {
        super(message);
        }

    public ValueSourceResolutionError(String message, Throwable cause)
        {
        super(message, cause);
        }
    }



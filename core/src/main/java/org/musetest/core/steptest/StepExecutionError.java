package org.musetest.core.steptest;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExecutionError extends Exception
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



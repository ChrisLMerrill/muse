package org.musetest.core;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MuseExecutionError extends Exception
    {
    public MuseExecutionError(String message)
        {
        super(message);
        }

    public MuseExecutionError(String message, Throwable cause)
        {
        super(message, cause);
        }
    }



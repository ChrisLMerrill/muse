package org.musetest.core.values;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceResolutionError extends MuseExecutionError
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



package org.museautomation.builtins.value.property;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // API ok for external use
public class PropertyResolutionError extends MuseExecutionError
    {
    public PropertyResolutionError(String message)
        {
        super(message);
        }

    public PropertyResolutionError(String message, Throwable cause)
        {
        super(message, cause);
        }
    }



package org.museautomation.core.values;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NullNotAllowedError extends ValueSourceResolutionError
    {
    public NullNotAllowedError(MuseValueSource source)
        {
        super("configuration requires a non-null parameter, but the value source (" + source.getDescription() + "), has a null value.");
        }
    }



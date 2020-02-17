package org.museautomation.core.steptask;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WrongTypeError extends ValueSourceResolutionError
    {
    public WrongTypeError(MuseValueSource source, Object value, Class type)
        {
        super(String.format("The value returned by the value source (%s), was not the expected type: %s. A %s was expected", source.getDescription(), value.getClass().getSimpleName(), type.getSimpleName()));
        }
    }



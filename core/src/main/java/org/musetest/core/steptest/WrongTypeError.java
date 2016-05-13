package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class WrongTypeError extends ValueSourceResolutionError
    {
    public WrongTypeError(MuseValueSource source, Object value)
        {
        super("The value returned by the value source (" + source.getDescription() + "), was not the expected type: " + value.getClass().getSimpleName());
        }
    }



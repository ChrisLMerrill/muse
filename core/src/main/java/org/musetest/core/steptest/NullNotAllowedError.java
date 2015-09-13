package org.musetest.core.steptest;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NullNotAllowedError extends ValueSourceResolutionError
    {
    public NullNotAllowedError(MuseValueSource source)
        {
        super("Step configuration requires a non-null parameter, but the value source (" + source.getDescription() + "), has a null value.");
        }
    }



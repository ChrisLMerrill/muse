package org.musetest.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Indicates the ValueSourceStringExpressionSupport class for this source type.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseSourceSESImplementation
    {
    Class value();
    }

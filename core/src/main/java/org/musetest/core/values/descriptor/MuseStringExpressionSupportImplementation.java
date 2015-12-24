package org.musetest.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Indicates the class that provides arValueSourceStringExpressionSupport for the annotated class.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseStringExpressionSupportImplementation
    {
    Class value();
    }

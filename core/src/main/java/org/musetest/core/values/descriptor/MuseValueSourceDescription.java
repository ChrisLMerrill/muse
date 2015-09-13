package org.musetest.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Define the one-line description of the value source.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseValueSourceDescription
    {
    String value();
    }

package org.museautomation.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Declares the human-readable name for this value source.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseValueSourceName
    {
    String value();
    }

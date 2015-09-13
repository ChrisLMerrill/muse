package org.musetest.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Indicate how this value source type should be grouped in the UI relative to other steps.
 *
 * Dots ('.') in the name indicate sub-groups.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseValueSourceTypeGroup
    {
    String value();
    }

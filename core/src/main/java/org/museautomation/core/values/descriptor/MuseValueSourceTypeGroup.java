package org.museautomation.core.values.descriptor;

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

    @SuppressWarnings("unused")   // used externally (ValueSourceTypeList)
    String DONT_SHOW = "!hide!";  // use this group name to not show the VS in the list
    }

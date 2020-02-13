package org.museautomation.core.step.descriptor;

import java.lang.annotation.*;

/**
 * A string the provides a short description of the step type. When describing a specific instance, @MuseInlineEditString will
 * be used in its place.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseStepLongDescription
    {
    String value();
    }

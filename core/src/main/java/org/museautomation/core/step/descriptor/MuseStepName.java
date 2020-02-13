package org.museautomation.core.step.descriptor;

import java.lang.annotation.*;

/**
 * Declares the human-readable name for this step
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseStepName
    {
    String value();
    }

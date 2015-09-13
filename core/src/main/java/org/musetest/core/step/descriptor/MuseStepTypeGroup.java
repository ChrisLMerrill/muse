package org.musetest.core.step.descriptor;

import java.lang.annotation.*;

/**
 * Indicate how this step type should be group in the UI relative to other steps.
 *
 * Dots ('.') in the name indicate sub-groups.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseStepTypeGroup
    {
    String value();
    }

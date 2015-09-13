package org.musetest.core.step.descriptor;

import java.lang.annotation.*;

/**
 * A string the provides a dynamically filled description of the step when it has been configured.
 *
 * Note that this overrides MuseStepShortDescription when an instance is supplied.
 *
 * @see MuseStepShortDescription
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseInlineEditString
    {
    String value();
    }

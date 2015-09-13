package org.musetest.core.step.descriptor;

import java.lang.annotation.*;

/**
 * Declares the unique id for this type within the context of Muse resource parsing and lookup.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseStepDescriptorImplementation
    {
    Class value();
    }

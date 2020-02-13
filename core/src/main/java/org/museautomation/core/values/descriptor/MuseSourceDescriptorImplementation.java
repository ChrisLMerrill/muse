package org.museautomation.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Configures a full implementation of the MuseSourceDescriptor, rather than inferring this
 * from the class and annotation information.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseSourceDescriptorImplementation
    {
    Class value();
    }

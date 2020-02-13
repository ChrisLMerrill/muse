package org.museautomation.core.values.descriptor;

import java.lang.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseSubsourceDescriptors
    {
    MuseSubsourceDescriptor[] value();
    }

package org.musetest.core.values.descriptor;

import java.lang.annotation.*;

/**
 * Define the one-line description of the value source.
 *
 *  @author Christopher L Merrill (see LICENSE.txt for license details) .
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MuseSubsourceDescriptors.class)
public @interface MuseSubsourceDescriptor
    {
    String displayName();
    String description();
    SubsourceDescriptor.Type type();
    String name() default "";
    int index() default -1;
    boolean optional() default false;
    Class resolutionType() default Object.class;
    }

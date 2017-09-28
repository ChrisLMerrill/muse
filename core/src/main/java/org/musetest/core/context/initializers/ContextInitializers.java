package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.resource.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ContextInitializers
    {
    public static void setup(MuseExecutionContext context)
        {
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceAttributes(new ContextInitializerConfiguration.ContextInitializerResourceType()));
        for (ResourceToken resource : resources)
            context.addInitializer(((ContextInitializerConfiguration) resource.getResource()).createInitializer());
        }
    }



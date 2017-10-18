package org.musetest.core.context.initializers;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ContextInitializers
    {
	/**
	 * Finds all the context initializers in the project and applies them to the supplied context.
	 */
	public static void setup(MuseExecutionContext context) throws MuseExecutionError
		{
        final List<ResourceToken> resources = context.getProject().getResourceStorage().findResources(new ResourceAttributes(new ContextInitializersConfiguration.ContextInitializersConfigurationResourceType()));
        // TODO check the condition
        // TODO catch failures, continue applying initializers
        for (ResourceToken resource : resources)
        	applyConditionally((ContextInitializersConfiguration) resource.getResource(), context);
        }

    public static void applyConditionally(ContextInitializersConfiguration configs, MuseExecutionContext context) throws MuseExecutionError
	    {
	    if (configs.shouldApplyToTest(context))
		    for (ContextInitializerConfiguration config : configs.getInitializers())
			    if (config.shouldApply(context))
			    	context.addInitializer(config.createInitializer(context.getProject()));
	    }
    }



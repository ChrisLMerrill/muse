package org.museautomation.core.plugins;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.generic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // available to extensions
public abstract class GenericConfigurableSteppedTaskPlugin extends GenericConfigurablePlugin
	{
	public GenericConfigurableSteppedTaskPlugin(GenericResourceConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof SteppedTaskExecutionContext;
		}
	}

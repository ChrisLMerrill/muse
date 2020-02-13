package org.museautomation.core.plugins;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.generic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class GenericConfigurableTestPlugin extends GenericConfigurablePlugin
	{
	public GenericConfigurableTestPlugin(GenericResourceConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof TestExecutionContext;
		}

	@Override
	public void shutdown()
		{

		}
	}

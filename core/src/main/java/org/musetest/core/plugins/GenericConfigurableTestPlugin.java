package org.musetest.core.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.generic.*;

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
	}

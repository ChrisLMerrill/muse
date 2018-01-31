package org.musetest.core.plugins;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")  // available to extensions
public abstract class GenericConfigurableSteppedTestPlugin extends GenericConfigurablePlugin
	{
	public GenericConfigurableSteppedTestPlugin(GenericResourceConfiguration configuration)
		{
		super(configuration);
		}

	@Override
	protected boolean applyToContextType(MuseExecutionContext context)
		{
		return context instanceof SteppedTestExecutionContext;
		}
	}

package org.musetest.core.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class GenericConfigurablePlugin implements MusePlugin
	{
	public GenericConfigurablePlugin(GenericResourceConfiguration configuration)
		{
		_configuration = configuration;
		}

	@Override
	public void conditionallyAddToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError
		{
		if (!applyToContextType(context))
			return;
		if (automatic)
			{
			if (!applyAutomatically(context))
				return;
			}
		if (applyToThisTest(context))
			context.addPlugin(this);
		}

	protected abstract boolean applyToContextType(MuseExecutionContext context);

	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	protected boolean applyAutomatically(MuseExecutionContext context) throws MuseExecutionError
		{
		MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), AUTO_APPLY_PARAM, true, context.getProject());
		return BaseValueSource.getValue(source, context, false, Boolean.class);
		}

	protected boolean applyToThisTest(MuseExecutionContext context) throws MuseExecutionError
		{
		MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), APPLY_CONDITION_PARAM, true, context.getProject());
		return BaseValueSource.getValue(source, context, false, Boolean.class);
		}

	protected GenericResourceConfiguration _configuration;

	public final static String AUTO_APPLY_PARAM = "auto-apply";
	public final static String APPLY_CONDITION_PARAM = "apply-condition";
	}



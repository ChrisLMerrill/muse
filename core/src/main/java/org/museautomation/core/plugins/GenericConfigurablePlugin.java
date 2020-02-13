package org.museautomation.core.plugins;

import org.museautomation.core.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.values.*;

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
	public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError
		{
		if (!applyToContextType(context))
			return false;
		if (automatic)
			{
			if (!applyAutomatically(context))
				return false;
			}
		if (!applyToThisTest(context))
			return false;

		context.addPlugin(this);
		return true;
		}

	@Override
	public void shutdown()
		{

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

	@Override
	public String getId()
		{
		return _configuration.getId();
		}

	protected GenericResourceConfiguration _configuration;

	public final static String AUTO_APPLY_PARAM = "auto-apply";
	public final static String APPLY_CONDITION_PARAM = "apply-condition";
	}

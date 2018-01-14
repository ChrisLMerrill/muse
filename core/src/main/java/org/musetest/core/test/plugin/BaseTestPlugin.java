package org.musetest.core.test.plugin;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseTestPlugin implements TestPlugin
	{
	public BaseTestPlugin(GenericResourceConfiguration configuration)
		{
		_configuration = configuration;
		}

	@Override
	public boolean applyAutomatically(MuseExecutionContext context) throws MuseExecutionError
		{
		MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), AUTO_APPLY_PARAM, true, context.getProject());
		return BaseValueSource.getValue(source, context, false, Boolean.class);
		}

	@Override
	public boolean applyToThisTest(MuseExecutionContext context) throws MuseExecutionError
		{
		MuseValueSource source = BaseValueSource.getValueSource(_configuration.parameters(), APPLY_CONDITION_PARAM, true, context.getProject());
		return BaseValueSource.getValue(source, context, false, Boolean.class);
		}

	protected GenericResourceConfiguration _configuration;

	public final static String AUTO_APPLY_PARAM = "auto-apply";
	public final static String APPLY_CONDITION_PARAM = "apply-condition";
	}



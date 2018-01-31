package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.plugins.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTestPlugin implements MusePlugin
	{
	MockTestPlugin(boolean apply_auto, boolean apply_test)
		{
		_apply_auto = apply_auto;
		_apply_test = apply_test;
		}

	@Override
	public void conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
		{
		if (!_apply_auto && automatic)
			return;

		if (_apply_test)
			context.addPlugin(this);
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{

		}

	private final boolean _apply_auto;
	private final boolean _apply_test;
	}



package org.museautomation.core.task.plugins;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTestPlugin implements MusePlugin
	{
	@SuppressWarnings("WeakerAccess")
    public MockTestPlugin(boolean apply_auto, boolean apply_test)
		{
		_apply_auto = apply_auto;
		_apply_test = apply_test;
		}

	@Override
	public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
		{
		if (!_apply_auto && automatic)
			return false;

		if (!_apply_test)
			return false;

		context.addPlugin(this);
		return true;
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{

		}

	@Override
	public String getId()
		{
		return _id;
		}

	@Override
	public void shutdown()
		{

		}

	private final boolean _apply_auto;
	private final boolean _apply_test;
	public String _id = "no/id";
	}



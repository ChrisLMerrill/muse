package org.musetest.core.test.plugins;

import org.musetest.core.*;
import org.musetest.core.resource.generic.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.test.plugin.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTestPlugin extends BaseTestPlugin
	{
	public MockTestPlugin(boolean apply_auto, boolean apply_test)
		{
		super(new GenericResourceConfiguration()
			{
			@Override
			public ResourceType getType()
				{
				return new MockPluginConfiguration.MockPluginConfigurationType();
				}
			});
		_apply_auto = apply_auto;
		_apply_test = apply_test;
		}

	@Override
	public String getType()
		{
		return MockPluginConfiguration.TYPE_ID;
		}

	@Override
	protected boolean applyAutomatically(MuseExecutionContext context)
		{
		return _apply_auto;
		}

	@Override
	protected boolean applyToThisTest(MuseExecutionContext context)
		{
		return _apply_test;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{

		}

	private final boolean _apply_auto;
	private final boolean _apply_test;
	}



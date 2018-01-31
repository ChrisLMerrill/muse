package org.musetest.core.test.plugins;

import org.musetest.core.plugins.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockPluginConfiguration extends BaseMuseResource implements PluginConfiguration
	{
	MockPluginConfiguration(boolean apply_auto, boolean apply_test)
		{
		_apply_auto = apply_auto;
		_apply_test = apply_test;
		}

	@Override
	public ResourceType getType()
		{
		return new MockPluginConfigurationType();
		}

	@Override
	public MusePlugin createPlugin()
		{
		return new MockTestPlugin(_apply_auto, _apply_test);
		}

	private final boolean _apply_auto;
	private final boolean _apply_test;

	public final static String TYPE_ID = "mock-plugin";

	public static class MockPluginConfigurationType extends ResourceType
		{
		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public MockPluginConfigurationType()
			{
			super(TYPE_ID, "Mock Plugin", MockPluginConfiguration.class);
			}
		}
	}

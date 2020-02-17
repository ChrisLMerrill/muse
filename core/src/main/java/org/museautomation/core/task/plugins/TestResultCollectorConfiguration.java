package org.museautomation.core.task.plugins;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-result-collector")
@Deprecated
@SuppressWarnings("WeakerAccess")  // instantiated by reflection
public class TestResultCollectorConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new TestResultCollectorConfigurationType();
		}

	@Override
	public MusePlugin createPlugin()
		{
		throw new IllegalStateException("This class is deprecated. Should have been converted when opening project");
		}

	public final static String TYPE_ID = TestResultCollectorConfiguration.class.getAnnotation(MuseTypeId.class).value();

	public static class TestResultCollectorConfigurationType extends ResourceSubtype
		{
		@Override
		public TestResultCollectorConfiguration create()
			{
            throw new IllegalStateException("This class is deprecated. Should have been converted when opening project");
			}

        @Override
        public boolean isInternalUseOnly()
            {
            return true;
            }

        @SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TestResultCollectorConfigurationType()
			{
			super(TYPE_ID, "Test Result Calculator", TaskResultCollector.class, new PluginConfigurationResourceType());
			}
		}
	}

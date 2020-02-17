package org.museautomation.core.suite.plugins;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-suite-result-counter")
@Deprecated
@SuppressWarnings("unused") // used by reflection
public class TestSuiteResultCounterConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public TaskSuiteResultCounter createPlugin()
		{
        throw new IllegalStateException("The TestSuiteResultCounter has been superceeded by the TaskSuiteResultCounter. This should have been converted when read from file.");
		}

	@Override
	public ResourceType getType()
		{
		return new TestSuiteResultCounterType();
		}

	public final static String TYPE_ID = TestSuiteResultCounterConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class TestSuiteResultCounterType extends ResourceSubtype
		{
		@Override
		public TestSuiteResultCounterConfiguration create()
			{
            throw new IllegalStateException("The TestSuiteResultCounter has been superceeded by the TaskSuiteResultCounter. This should have been converted when read from file.");
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "(obsolete)");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TestSuiteResultCounterType()
			{
			super(TYPE_ID, "(obsolete)", TaskSuiteResultCounter.class, new PluginConfiguration.PluginConfigurationResourceType());
			}

        @Override
        public boolean isInternalUseOnly()
            {
            return true;
            }
        }
	}
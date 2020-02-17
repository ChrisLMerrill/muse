package org.museautomation.builtins.plugins.init;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-defaults-initializer")
@Deprecated
@SuppressWarnings("unused")  // instantiated by reflection
public class TestDefaultsInitializerConfiguration extends GenericResourceConfiguration implements PluginConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new TestDefaultsInitializerType();
		}

	@Override
	public TaskDefaultsInitializer createPlugin()
		{
		throw new IllegalStateException("The TestDefaultsInitializerConfiguration has been superceeded by the TaskDefaultsInitializerConfiguration. This should have been converted when read from file.");
		}

	public final static String TYPE_ID = TestDefaultsInitializerConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class TestDefaultsInitializerType extends ResourceSubtype
		{
		@Override
		public TestDefaultsInitializerConfiguration create()
			{
            throw new IllegalStateException("The TestDefaultsInitializerConfiguration has been superceeded by the TaskDefaultsInitializerConfiguration. This should have been converted when read from file.");
			}

		@Override
		public ResourceDescriptor getDescriptor()
			{
			return new DefaultResourceDescriptor(this, "(obsolete)");
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public TestDefaultsInitializerType()
			{
			super(TYPE_ID, "Test Defaults Initializer", TestDefaultsInitializerConfiguration.class, new PluginConfigurationResourceType());
			}

        @Override
        public boolean isInternalUseOnly()
            {
            return true;
            }
        }
	}

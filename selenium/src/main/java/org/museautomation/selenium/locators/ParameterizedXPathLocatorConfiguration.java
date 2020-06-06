package org.museautomation.selenium.locators;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("parameterized-xpath-locator")
@MuseSubsourceDescriptor(displayName = "XPath", description = "Parameterized XPath, e.g. \"//button[@name='", type = SubsourceDescriptor.Type.Named, name = ParameterizedXPathLocatorConfiguration.XPATH, optional = true, defaultValue = "//element")
@SuppressWarnings("unused") // Instantiated by reflection
public class ParameterizedXPathLocatorConfiguration extends GenericResourceConfiguration
	{
	@Override
	public ResourceType getType()
		{
		return new ParameterizedXPathLocatorType();
		}

    public String getXPath(MuseExecutionContext context) throws MuseInstantiationException, ValueSourceResolutionError
        {
   		return getParameterAsString(context, XPATH);
   		}

	public final static String TYPE_ID = ParameterizedXPathLocatorConfiguration.class.getAnnotation(MuseTypeId.class).value();

	@SuppressWarnings("unused") // used by reflection
	public static class ParameterizedXPathLocatorType extends ResourceType
		{
		@Override
		public ParameterizedXPathLocatorConfiguration create()
			{
			return new ParameterizedXPathLocatorConfiguration();
			}

		@SuppressWarnings("WeakerAccess")  // instantiated by reflection
		public ParameterizedXPathLocatorType()
			{
			super(TYPE_ID, "Parameterized XPath Locator", ParameterizedXPathLocatorConfiguration.class);
			}
		}

    final static String XPATH = "xpath";
	}

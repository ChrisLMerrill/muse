package org.museautomation.selenium.locators;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("element-name")
@MuseValueSourceName("Element by Field Name")
@MuseValueSourceTypeGroup("Selenium.Element.Locate")
@MuseValueSourceShortDescription("Locates an element by field name")
@MuseValueSourceLongDescription("Locate a WebElement in the current browser window by calling driver.findElement() with Selenium's built-in ByName locator.")
@MuseStringExpressionSupportImplementation(NameElementValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the element", type = SubsourceDescriptor.Type.Single)
@MuseSubsourceDescriptor(displayName = "Find Multiple", description = "If true, look for 0 to many matching elements and resolve as a list. If false (default) return first match and fail if none found", type = SubsourceDescriptor.Type.Named, name = ElementByLocatorValueSource.MULTIPLE_PARAM, defaultValue = "false", optional = true)
public class NameElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public NameElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(MuseExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.name(locator_string);
        }

    public final static String TYPE_ID = NameElementValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NameElementValueSource.TYPE_ID, STRING_EXPRESSION_ID);
            }

        public static final String STRING_EXPRESSION_ID = "name";
        }
    }

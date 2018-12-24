package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("css-selector")
@MuseValueSourceName("Element by CSS Selector")
@MuseValueSourceTypeGroup("Element.Locate")
@MuseValueSourceShortDescription("Locate a element by CSS Selector")
@MuseValueSourceLongDescription("Locate a WebElement in the current browser window by calling driver.findElement() with Selenium's built-in ByCssSelector locator.")
@MuseStringExpressionSupportImplementation(CssElementValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Selector", description = "A CSS selector string", type = SubsourceDescriptor.Type.Single)
@MuseSubsourceDescriptor(displayName = "Find Multiple", description = "If true, look for 0 to many matching elements and resolve as a list. If false (default) return first match and fail if none found", type = SubsourceDescriptor.Type.Named, name = ElementByLocatorValueSource.MULTIPLE_PARAM, defaultValue = "false")
public class CssElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CssElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(MuseExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.cssSelector(locator_string);
        }

    public final static String TYPE_ID = CssElementValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends ElementByLocatorValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(CssElementValueSource.TYPE_ID, STRING_EXPRESSION_ID);
            }

        public static final String STRING_EXPRESSION_ID = "css";
        }
    }

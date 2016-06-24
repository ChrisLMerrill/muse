package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.conditions.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("css-selector")
@MuseValueSourceName("Element by CSS Selector")
@MuseValueSourceTypeGroup("Element.Locate")
@MuseValueSourceShortDescription("Locate a element by CSS Selector")
@MuseValueSourceLongDescription("Locate a WebElement in the current browser window by calling driver.findElement() with Selenium's built-in ByCssSelector locator.")
@MuseStringExpressionSupportImplementation(CssElementValueSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Selector", description = "A CSS selector string", type = SubsourceDescriptor.Type.Single)
public class CssElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CssElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.cssSelector(locator_string);
        }

    public final static String TYPE_ID = CssElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

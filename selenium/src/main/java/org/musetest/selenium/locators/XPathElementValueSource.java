package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("element-xpath")
@MuseValueSourceName("Element by XPath")
@MuseValueSourceTypeGroup("Element.Locate")
@MuseValueSourceShortDescription("Locate an element by XPath")
@MuseValueSourceLongDescription("Locate a WebElement in the current browser window by calling driver.findElement() with Selenium's built-in ByXPath locator.")
@MuseStringExpressionSupportImplementation(XPathElementValueSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "XPath", description = "An XPath string", type = SubsourceDescriptor.Type.Single)
public class XPathElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public XPathElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.xpath(locator_string);
        }

    public final static String TYPE_ID = XPathElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

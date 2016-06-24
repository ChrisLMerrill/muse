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
@MuseTypeId("element-id")
@MuseValueSourceName("Element by ID")
@MuseValueSourceTypeGroup("Element.Locate")
@MuseValueSourceShortDescription("Locate an element by id")
@MuseValueSourceLongDescription("Locate a WebElement in the current browser window by calling driver.findElement() with Selenium's built-in ById locator.")
@MuseStringExpressionSupportImplementation(IdElementValueSourceStringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Id", description = "Id of the element", type = SubsourceDescriptor.Type.Single)
public class IdElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public IdElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.id(locator_string);
        }

    public final static String TYPE_ID = IdElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

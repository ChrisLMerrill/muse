package org.museautomation.selenium.values;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("window-handle")
@MuseValueSourceName("Window Handle")
@MuseValueSourceTypeGroup("Selenium")
@MuseValueSourceShortDescription("Get current window handle")
@MuseValueSourceLongDescription("Returns the handle of the current window/tab")
@MuseStringExpressionSupportImplementation(LastWindowHandleSource.StringExpressionSupport.class)
public class LastWindowHandleSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public LastWindowHandleSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getWindowHandle();
        }

    public final static String TYPE_ID = LastWindowHandleSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, LastWindowHandleSource.TYPE_ID);
            }

        public final static String NAME = "window";
        }
    }

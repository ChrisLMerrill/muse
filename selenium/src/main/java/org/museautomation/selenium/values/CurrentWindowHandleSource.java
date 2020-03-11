package org.museautomation.selenium.values;

import org.museautomation.core.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("window-handle")
@MuseValueSourceName("Window Handle")
@MuseValueSourceTypeGroup("Selenium")
@MuseValueSourceShortDescription("Get current window handle")
@MuseValueSourceLongDescription("Returns the handle of the current window/tab")
@MuseStringExpressionSupportImplementation(CurrentWindowHandleSource.StringExpressionSupport.class)
public class CurrentWindowHandleSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CurrentWindowHandleSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getWindowHandle();
        }

    public final static String TYPE_ID = CurrentWindowHandleSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, CurrentWindowHandleSource.TYPE_ID);
            }

        public final static String NAME = "window";
        }
    }

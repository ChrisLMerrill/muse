package org.museautomation.selenium.values;

import org.museautomation.core.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("window-handles")
@MuseValueSourceName("Window Handles")
@MuseValueSourceTypeGroup("Selenium")
@MuseValueSourceShortDescription("Get list of all window handles")
@MuseValueSourceLongDescription("Returns a list of window handles that are currently open.")
@MuseStringExpressionSupportImplementation(WindowHandlesSource.StringExpressionSupport.class)
public class WindowHandlesSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public WindowHandlesSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Set<String> set = getDriver(context).getWindowHandles();
        return List.copyOf(set);
        }

    public final static String TYPE_ID = WindowHandlesSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, WindowHandlesSource.TYPE_ID);
            }

        public final static String NAME = "windows";
        }
    }

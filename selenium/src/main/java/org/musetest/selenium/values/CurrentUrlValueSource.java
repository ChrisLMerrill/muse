package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("current-url")
@MuseValueSourceName("Page URL")
@MuseValueSourceTypeGroup("Web Page")
@MuseValueSourceShortDescription("URL of the current page")
@MuseValueSourceLongDescription("Retrieves the URL of the current browser window by calling driver.getCurrentUrl().")
@MuseStringExpressionSupportImplementation(CurrentUrlValueSource.StringExpressionSupport.class)
public class CurrentUrlValueSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CurrentUrlValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getCurrentUrl();
        }

    public final static String TYPE_ID = CurrentUrlValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, CurrentUrlValueSource.TYPE_ID);
            }

        public final static String NAME = "url";
        }
    }

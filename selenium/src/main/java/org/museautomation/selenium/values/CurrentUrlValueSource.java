package org.museautomation.selenium.values;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;

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
    public CurrentUrlValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String url = getDriver(context).getCurrentUrl();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), url));
        return url;
        }

    public final static String TYPE_ID = CurrentUrlValueSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, CurrentUrlValueSource.TYPE_ID);
            }

        public final static String NAME = "url";
        }
    }

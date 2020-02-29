package org.museautomation.selenium.values;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page-title")
@MuseValueSourceName("Page Title")
@MuseValueSourceTypeGroup("Selenium.Page")
@MuseValueSourceShortDescription("Title of the current page")
@MuseValueSourceLongDescription("Retrieves the page title of the current browser window by calling driver.getTitle().")
@MuseStringExpressionSupportImplementation(PageTitleValueSource.StringExpressionSupport.class)
public class PageTitleValueSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PageTitleValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String title = getDriver(context).getTitle();
        context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), title));
        return title;
        }

    public final static String TYPE_ID = PageTitleValueSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, PageTitleValueSource.TYPE_ID);
            }

        public final static String NAME = "title";
        }
    }

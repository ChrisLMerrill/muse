package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page-title")
@MuseValueSourceName("Page Title")
@MuseValueSourceTypeGroup("Web Page")
@MuseValueSourceShortDescription("Title of the current page")
@MuseValueSourceLongDescription("Retrieves the page title of the current browser window by calling driver.getTitle().")
@MuseStringExpressionSupportImplementation(PageTitleValueSourceStringExpressionSupport.class)
public class PageTitleValueSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PageTitleValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getTitle();
        }

    public final static String TYPE_ID = PageTitleValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

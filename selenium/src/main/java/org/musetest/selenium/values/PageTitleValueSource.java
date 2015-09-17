package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page-title")
@MuseValueSourceName("Page Title")
@MuseValueSourceInstanceDescription("<title>")
@MuseValueSourceTypeGroup("Web Page")
@MuseValueSourceShortDescription("Title of the current page")
@MuseValueSourceLongDescription("Retrieves the page title of the current browser window by calling driver.getTitle().")
public class PageTitleValueSource extends BrowserValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PageTitleValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getTitle();
        }

    @Override
    public String getDescription()
        {
        return NAME;
        }

    public final static String NAME = PageTitleValueSource.class.getAnnotation(MuseValueSourceInstanceDescription.class).value();
    public final static String TYPE_ID = PageTitleValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

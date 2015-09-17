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
@MuseTypeId("page-source")
@MuseValueSourceName("Page Source")
@MuseValueSourceInstanceDescription("<source>")
@MuseValueSourceTypeGroup("Web Page")
@MuseValueSourceShortDescription("Full source of the current page")
@MuseValueSourceLongDescription("Retrieves the page source of the current browser window by calling driver.getPageSource().")
public class PageSourceValueSource extends BrowserValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PageSourceValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getPageSource();
        }

    @Override
    public String getDescription()
        {
        return NAME;
        }

    public final static String NAME = PageSourceValueSource.class.getAnnotation(MuseValueSourceInstanceDescription.class).value();
    public final static String TYPE_ID = PageSourceValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

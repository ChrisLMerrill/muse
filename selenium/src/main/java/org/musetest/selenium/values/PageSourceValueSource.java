package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page-source")
@MuseValueSourceName("Page Source")
@MuseValueSourceTypeGroup("Web Page")
@MuseValueSourceShortDescription("Full source of the current page")
@MuseValueSourceLongDescription("Retrieves the page source of the current browser window by calling driver.getPageSource().")
@MuseStringExpressionSupportImplementation(PageSourceValueSource.StringExpressionSupport.class)
public class PageSourceValueSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public PageSourceValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getPageSource();
        }

    public final static String TYPE_ID = PageSourceValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends SimpleWebdriverValueSourceStringExpressionSupport
        {
        public StringExpressionSupport()
            {
            super(NAME, PageSourceValueSource.TYPE_ID);
            }

        public final static String NAME = "source";
        }
    }

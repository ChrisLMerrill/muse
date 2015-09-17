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
@MuseTypeId("current-url")
@MuseValueSourceName("Page URL")
@MuseValueSourceInstanceDescription("<url>")
@MuseValueSourceTypeGroup("Web Page")
@MuseValueSourceShortDescription("URL of the current page")
@MuseValueSourceLongDescription("Retrieves the URL of the current browser window by calling driver.getCurrentUrl().")
public class CurrentUrlValueSource extends BrowserValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public CurrentUrlValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return getDriver(context).getCurrentUrl();
        }

    @Override
    public String getDescription()
        {
        return NAME;
        }

    public final static String NAME = CurrentUrlValueSource.class.getAnnotation(MuseValueSourceInstanceDescription.class).value();
    public final static String TYPE_ID = CurrentUrlValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

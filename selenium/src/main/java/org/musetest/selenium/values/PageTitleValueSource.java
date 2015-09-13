package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("page-title")
@MuseValueSourceName("Title of Current Page")
@MuseValueSourceDescription("<title>")
@MuseValueSourceTypeGroup("Web Page")
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

    public final static String NAME = PageTitleValueSource.class.getAnnotation(MuseValueSourceDescription.class).value();
    public final static String TYPE_ID = PageTitleValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

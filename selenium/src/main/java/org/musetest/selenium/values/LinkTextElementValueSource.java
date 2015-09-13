package org.musetest.selenium.values;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("element-linktext")
@MuseValueSourceName("Element by Link Text")
@MuseValueSourceDescription("<linktext:{source}>")
@MuseValueSourceTypeGroup("Element")
public class LinkTextElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public LinkTextElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.linkText(locator_string);
        }

    @Override
    public String getDescription()
        {
        return NAME;
        }

    public final static String NAME = LinkTextElementValueSource.class.getAnnotation(MuseValueSourceDescription.class).value();
    public final static String TYPE_ID = LinkTextElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

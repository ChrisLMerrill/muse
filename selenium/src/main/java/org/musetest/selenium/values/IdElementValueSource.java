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
@MuseTypeId("element-id")
@MuseValueSourceName("Element by ID")
@MuseValueSourceDescription("<id:{source}>")
@MuseValueSourceTypeGroup("Element")
public class IdElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public IdElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.id(locator_string);
        }

    @Override
    public String getDescription()
        {
        return NAME;
        }

    public final static String NAME = IdElementValueSource.class.getAnnotation(MuseValueSourceDescription.class).value();
    public final static String TYPE_ID = IdElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

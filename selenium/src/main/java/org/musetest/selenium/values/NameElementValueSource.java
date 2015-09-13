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
@MuseTypeId("element-name")
@MuseValueSourceName("Element by Field Name")
@MuseValueSourceDescription("<name:{source}>")
@MuseValueSourceTypeGroup("Element")
public class NameElementValueSource extends ElementByLocatorValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public NameElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    protected By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError
        {
        return By.name(locator_string);
        }

    @Override
    public String getDescription()
        {
        return NAME.substring(0, NAME.indexOf(":")) + _locator_source.getDescription();
        }

    public final static String NAME = NameElementValueSource.class.getAnnotation(MuseValueSourceDescription.class).value();
    public final static String TYPE_ID = NameElementValueSource.class.getAnnotation(MuseTypeId.class).value();
    }

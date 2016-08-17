package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ElementByLocatorValueSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    protected ElementByLocatorValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _locator_source = getValueSource(config, true, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String locator_string = getValue(_locator_source, context, true, String.class);
        try
            {
            WebElement element = getDriver(context).findElement(createBy(context, locator_string));
            context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), element));
            return element;
            }
        catch (NoSuchElementException e)
            {
            context.raiseEvent(new ValueSourceResolvedEvent(getDescription(), "element not found using locator string: " + locator_string));
            return null;
            }
        }

    protected abstract By createBy(MuseExecutionContext context, String locator_string) throws ValueSourceResolutionError;

    protected final MuseValueSource _locator_source;
    }

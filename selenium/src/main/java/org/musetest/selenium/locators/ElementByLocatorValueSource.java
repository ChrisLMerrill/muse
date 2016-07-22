package org.musetest.selenium.locators;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
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
        ValueSourceConfiguration locator_config = config.getSource();
        if (locator_config == null)
            throw new MuseInstantiationException("XPathElementValueSource requires a source for the xpath.");
        _locator_source = locator_config.createSource(project);
        }

    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        String locator_string = _locator_source.resolveValue(context).toString();
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

    protected abstract By createBy(StepExecutionContext context, String locator_string) throws ValueSourceResolutionError;

    protected final MuseValueSource _locator_source;
    }

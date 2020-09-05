package org.museautomation.selenium.locators;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // used via reflection, public API
public abstract class ElementByLocatorValueSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    protected ElementByLocatorValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _locator_source = getValueSource(config, true, project);
        _find_multiple = getValueSource(config, MULTIPLE_PARAM, false, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String locator_string = getValue(_locator_source, context, true, String.class);
        boolean multiple = false;
        if (_find_multiple != null)
            {
            Boolean fm = getValue(_find_multiple, context, true, Boolean.class);
            if (fm != null && fm)
                multiple = true;
            }
        try
            {
            By by = createBy(context, locator_string);

            if (multiple)
                {
                List<WebElement> elements = getSearchContext(context).findElements(by);
                context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), elements.size() + " elements found"));
                return elements;
                }
            else
                {
                WebElement element = getSearchContext(context).findElement(by);
                context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), element));
                return element;
                }
            }
        catch (NoSuchElementException e)
            {
            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), "element not found using locator string: " + locator_string));
            return null;
            }
        }

    protected abstract By createBy(MuseExecutionContext context, String locator_string) throws ValueSourceResolutionError;

    protected final MuseValueSource _locator_source;
    protected final MuseValueSource _find_multiple;

    public final static String MULTIPLE_PARAM = "find-multiple";
    public final static String MULTIPLE_SYNTAX = "multiple";
    }
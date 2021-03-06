package org.museautomation.selenium;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseElementValueSource extends BaseSeleniumValueSource
    {
    public BaseElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _element_source = findElementSource(config, project);
        }

    protected MuseValueSource findElementSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        return getValueSource(config, true, project);
        }

    public MuseValueSource getElementSource()
        {
        return _element_source;
        }

    public WebElement resolveElementSource(MuseExecutionContext context, boolean required) throws ValueSourceResolutionError
        {
        Object element = _element_source.resolveValue(context);
        if (element == null)
            {
            if (required)
                throw new ValueSourceResolutionError("Cannot locate the element: locator returned null");
            else
                return null;
            }
        if (element instanceof WebElement)
            return (WebElement) element;
        throw new ValueSourceResolutionError("The sub-source result should be a WebElement. Did not expect a " + element.getClass().getSimpleName());
        }

    MuseValueSource _element_source;
    }



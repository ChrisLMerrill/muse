package org.musetest.selenium;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseElementValueSource extends BaseSeleniumValueSource
    {
    public BaseElementValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        ValueSourceConfiguration source = config.getSource();
        if (source == null)
            throw new MuseInstantiationException("BaseElementValueSource requires a source for the element.");
        _element_source = source.createSource(project);
        }

    public MuseValueSource getElementSource()
        {
        return _element_source;
        }

    public WebElement resolveElementSource(StepExecutionContext context, boolean required) throws StepConfigurationError
        {
        Object element = _element_source.resolveValue(context);
        if (element == null)
            {
            if (required)
                throw new ValueSourceResolutionError("Cannot determine locate the element: locator returned null");
            else
                return null;
            }
        if (element instanceof WebElement)
            return (WebElement) element;
        throw new ValueSourceResolutionError("The sub-source result should be a WebElement. Did not expect a " + element.getClass().getSimpleName());
        }

    MuseValueSource _element_source;
    }



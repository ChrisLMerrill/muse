package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BrowserStep extends BaseStep
    {
    public BrowserStep(StepConfiguration configuration)
        {
        super(configuration);
        }

    protected WebDriver getDriver(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return BrowserStepExecutionContext.getDriver(context);
        }

    /**
     * Convenience method to get an element and handle error conditions.
     */
    protected WebElement getElement(MuseValueSource locator_source, StepExecutionContext context) throws StepExecutionError
        {
        Object element = locator_source.resolveValue(context);
        if (element == null)
            throw new StepExecutionError("The element source resolved to null");
        if (!(element instanceof WebElement))
            throw new StepExecutionError(String.format("The element source did not resolve to an element: %s: %s", element.getClass().getSimpleName(), element.toString()));
        return (WebElement) element;
        }
    }



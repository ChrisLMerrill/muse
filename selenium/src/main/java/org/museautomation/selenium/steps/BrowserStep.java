package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.museautomation.selenium.*;
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

    protected static WebDriver getDriver(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return BrowserStepExecutionContext.getDriver(context);
        }

    /**
     * Convenience method for subclasses to get an element and automatically handle not-found and wrong-type errors.
     *
     * @param locator_source The value source that will locate the element
     * @param context The context in which to resolve the source and find the element - the browser is acquired from the context.
     *
     * @return The desired element
     *
     * @throws StepExecutionError If the source resolves to null or the wrong type
     */
    @SuppressWarnings("WeakerAccess")  // API available to external users
    protected WebElement getElement(MuseValueSource locator_source, StepExecutionContext context) throws MuseExecutionError
        {
        Object element = locator_source.resolveValue(context);
        if (element == null)
            throw new StepExecutionError("The element source resolved to null");
        if (!(element instanceof WebElement))
            throw new StepExecutionError(String.format("The element source did not resolve to an element: %s: %s", element.getClass().getSimpleName(), element.toString()));
        return (WebElement) element;
        }
    }
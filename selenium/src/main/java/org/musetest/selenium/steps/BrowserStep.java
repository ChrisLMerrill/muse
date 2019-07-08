package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
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

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError
        {
        boolean retry = true;
        while (retry)
            try
                {
                return executeImplementation(context);
                }
            catch (UnhandledAlertException e)
                {

                // TODO if there is an alert handler use it and remove it
                // TODO   e.g getDriver(context).switchTo().alert().accept();
                // TODO else try = false;
                }
        context.raiseEvent(MessageEventType.create("Alert is blocking Selenium. Add the Alert Handler plugin or use the the Handle Next Alert step."));
        return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "Alert is blocking Selenium.");
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



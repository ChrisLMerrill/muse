package org.musetest.selenium;

import org.musetest.core.context.*;
import org.musetest.core.steptest.*;
import org.openqa.selenium.*;

/**
 * Utility class for storing browser-related stuff in the context. This was originally part
 * of a BrowserStep, but was also needed by BrowserValueSource. Didn't want to duplicate it.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BrowserStepExecutionContext
    {
    public static WebDriver getDriver(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = context.getTestExecutionContext().getVariable(DEFAULT_DRIVER_VARIABLE_NAME);
        if (value == null)
            throw new ValueSourceResolutionError("Browser driver not found - use the \"Open Browser\" step first.");

        if (!(value instanceof WebDriver))
            throw new ValueSourceResolutionError(DEFAULT_DRIVER_VARIABLE_NAME + " is not a WebDriver.  This is a reserved test variable name...don't do that!");

        return (WebDriver) value;
        }

    public static void putDriver(WebDriver driver, StepExecutionContext context)
        {
        context.getTestExecutionContext().setVariable(DEFAULT_DRIVER_VARIABLE_NAME, driver);
        }

    public final static String DEFAULT_DRIVER_VARIABLE_NAME = "_webdriver";
    }



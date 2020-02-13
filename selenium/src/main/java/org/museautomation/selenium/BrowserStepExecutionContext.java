package org.museautomation.selenium;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.values.*;
import org.museautomation.core.variables.*;
import org.openqa.selenium.*;

/**
 * Utility class for storing browser-related stuff in the context. This was originally part
 * of a BrowserStep, but was also needed by BrowserValueSource. Didn't want to duplicate it.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BrowserStepExecutionContext
    {
    public static WebDriver getDriver(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = context.getVariable(DEFAULT_DRIVER_VARIABLE_NAME, VariableQueryScope.Execution);
        if (value == null)
            throw new ValueSourceResolutionError("Browser driver not found - use the \"Open Browser\" step first.");

        if (!(value instanceof WebDriver))
            throw new ValueSourceResolutionError(DEFAULT_DRIVER_VARIABLE_NAME + " is not a WebDriver.  This is a reserved test variable name...don't do that!");

        return (WebDriver) value;
        }

    public static void putDriver(WebDriver driver, MuseExecutionContext context)
        {
        context.setVariable(DEFAULT_DRIVER_VARIABLE_NAME, driver, ContextVariableScope.Execution);
        }

    private final static String DEFAULT_DRIVER_VARIABLE_NAME = "_webdriver";
    }

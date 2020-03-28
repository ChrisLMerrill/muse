package org.museautomation.selenium;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.values.*;
import org.museautomation.core.variables.*;
import org.openqa.selenium.*;

import java.util.*;

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

    static SearchContext getSearchContext(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Stack<SearchContext> stack = getSearchContextStack(context);
        if (stack != null)
            return stack.peek();
        else
            return getDriver(context);
        }

    private static Stack<SearchContext> getSearchContextStack(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = context.getVariable(DEFAULT_SEARCH_STACK_VARIABLE_NAME, VariableQueryScope.Execution);
        if (value instanceof Stack)
            return (Stack<SearchContext>) value;
        else if (value == null)
            return null;
        else
            throw new ValueSourceResolutionError("The SearchContext stack should only contain SearchContexts. Found a " + value.getClass().getSimpleName());
        }

    public static void pushSearchContext(MuseExecutionContext context, SearchContext search) throws ValueSourceResolutionError
        {
        Stack<SearchContext> stack = getSearchContextStack(context);
        if (stack == null)
            {
            stack = new Stack<>();
            context.setVariable(DEFAULT_SEARCH_STACK_VARIABLE_NAME, stack, ContextVariableScope.Execution);
            }
        stack.push(search);
        }

    public static SearchContext popSearchContext(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        Stack<SearchContext> stack = getSearchContextStack(context);
        if (stack == null)
            throw new ValueSourceResolutionError("The SearchContext stack was not found. This could mean that the SearchContext was never pushed, or it was popped prematurely. Or the caller is confused.");

        SearchContext search = stack.pop();
        if (stack.isEmpty())
            context.setVariable(DEFAULT_SEARCH_STACK_VARIABLE_NAME, null, ContextVariableScope.Execution);
        return search;
        }

    @SuppressWarnings("WeakerAccess")  // public API
    public final static String DEFAULT_DRIVER_VARIABLE_NAME = "_webdriver";
    private final static String DEFAULT_SEARCH_STACK_VARIABLE_NAME = "_search_stack";
    }

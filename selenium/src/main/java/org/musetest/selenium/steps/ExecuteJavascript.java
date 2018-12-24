package org.musetest.selenium.steps;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.descriptor.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("execute-js")
@MuseStepName("Run Javascript")
@MuseInlineEditString("Run {script}")
@MuseStepIcon("glyph:FontAwesome:CODE")
@MuseStepTypeGroup("Selenium")
@MuseStepShortDescription("Run a script in the browser")
@MuseStepLongDescription("Resolve the script source to a string. Inject that into the browser and run it.")
@MuseSubsourceDescriptor(displayName = "Script", description = "The script to execute (as a text string)", type = SubsourceDescriptor.Type.Named, name = ExecuteJavascript.SCRIPT_PARAM)
@MuseSubsourceDescriptor(displayName = "Arguments", description = "Arguments to pass into the script (accessed as arguments[N])", type = SubsourceDescriptor.Type.List, name = ExecuteJavascript.ARGUMENTS_PARAM)
public class ExecuteJavascript extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public ExecuteJavascript(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _script = getValueSource(config, SCRIPT_PARAM, true, project);
        _arguments = getValueSource(config, ARGUMENTS_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String script = getValue(_script, context, false, String.class);
        Object[] arg_list = null;
        if (_arguments != null)
            {
            Object args = getValue(_arguments, context, true, Object.class);
            if (args instanceof List)
                arg_list = ((List) args).toArray(new Object[0]);
            else
                return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "The 'arguments' parameter must be a list. Instead, it was a " + args.getClass().getSimpleName());
            }

        WebDriver driver = getDriver(context);
        if (driver instanceof JavascriptExecutor)
            {
            Object result;
            try
                {
                result = ((JavascriptExecutor) driver).executeScript(script, arg_list);
                }
            catch (Exception e)
                {
                return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "Script threw an exception: " + e.getMessage());
                }

            String result_string = "null";
            if (result != null)
                result_string = result.toString();
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "Script result is: " + result_string);
            }
        else
            {
            throw new StepExecutionError("Cannot execute the script because this WebDriver does not implement JavascriptExecutor.");
            }
        }

    private MuseValueSource _script;
    private MuseValueSource _arguments;

    public final static String SCRIPT_PARAM = "script";
    public final static String ARGUMENTS_PARAM = "arguments";

    public final static String TYPE_ID = ExecuteJavascript.class.getAnnotation(MuseTypeId.class).value();
    }



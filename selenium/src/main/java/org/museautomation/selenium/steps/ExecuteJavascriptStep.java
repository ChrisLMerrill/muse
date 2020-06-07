package org.museautomation.selenium.steps;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.descriptor.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("execute-js")
@MuseStepName("Run Javascript")
@MuseInlineEditString("Run {script}")
@MuseStepIcon("glyph:FontAwesome:CODE")
@MuseStepTypeGroup("Selenium.Other")
@MuseStepShortDescription("Run a script in the browser")
@MuseStepLongDescription("Resolve the script source to a string. Inject that into the browser and run it.")
@MuseSubsourceDescriptor(displayName = "Script", description = "The script to execute (as a text string)", type = SubsourceDescriptor.Type.Named, name = ExecuteJavascriptStep.SCRIPT_PARAM)
@MuseSubsourceDescriptor(displayName = "Arguments", description = "Arguments to pass into the script (accessed as arguments[N])", type = SubsourceDescriptor.Type.List, name = ExecuteJavascriptStep.ARGUMENTS_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Return Variable", description = "Name of variable to store the returned value", type = SubsourceDescriptor.Type.Named, name = ExecuteJavascriptStep.RETURN_VARIABLE, optional = true)
public class ExecuteJavascriptStep extends BrowserStep
    {
    @SuppressWarnings("unused") // called via reflection
    public ExecuteJavascriptStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _script = getValueSource(config, SCRIPT_PARAM, true, project);
        _arguments = getValueSource(config, ARGUMENTS_PARAM, false, project);
        _return_var = getValueSource(config, RETURN_VARIABLE, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String script = getValue(_script, context, false, String.class);
        Object[] arg_list = new Object[0];
        if (_arguments != null)
            {
            Object args = getValue(_arguments, context, true, Object.class);
            if (args instanceof List)
                arg_list = ((List) args).toArray(new Object[0]);
            else
                return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "The 'arguments' parameter must be a list. Instead, it was a " + args.getClass().getSimpleName());
            }

        String return_varname = null;
        if (_return_var != null)
            return_varname = getValue(_return_var, context, true, String.class);

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

            if (return_varname != null)
                context.setVariable(return_varname, result);

            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "Script completed, returned: " + result);
            }
        else
            {
            throw new StepExecutionError("Cannot execute the script because this WebDriver does not implement JavascriptExecutor.");
            }
        }

    private MuseValueSource _script;
    private MuseValueSource _arguments;
    private MuseValueSource _return_var;

    public final static String SCRIPT_PARAM = "script";
    public final static String ARGUMENTS_PARAM = "arguments";
    public final static String RETURN_VARIABLE = "return_var";

    public final static String TYPE_ID = ExecuteJavascriptStep.class.getAnnotation(MuseTypeId.class).value();
    }



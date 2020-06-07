package org.museautomation.selenium.values;

import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.selenium.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("evaluate-js-in-browser")
@MuseValueSourceName("Evaluate Javascript in Page")
@MuseValueSourceTypeGroup("Selenium")
@MuseValueSourceShortDescription("Evaluate javascript in the current page in the browser")
@MuseValueSourceLongDescription("Resolve the script source to a string. Inject the script into the current browser page, evaluate it and return the result.")
@MuseStringExpressionSupportImplementation(EvaluateJavascriptSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Script", description = "The script to evaulate (as a text string)", type = SubsourceDescriptor.Type.Single)
@MuseSubsourceDescriptor(displayName = "Arguments", description = "Arguments to pass into the script (accessed as arguments[N])", type = SubsourceDescriptor.Type.List, name = EvaluateJavascriptSource.ARGUMENTS_PARAM, optional = true)
@SuppressWarnings("unused")  // instantiated via reflection
public class EvaluateJavascriptSource extends BaseSeleniumValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public EvaluateJavascriptSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _script = getValueSource(config, true, project);
        _arguments = getValueSource(config, ARGUMENTS_PARAM, false, project);
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        String script = getValue(_script, context, false, String.class);
        Object[] arg_list = new Object[0];
        if (_arguments != null)
            {
            Object args = getValue(_arguments, context, true, Object.class);
            if (args instanceof List)
                arg_list = ((List<Object>) args).toArray(new Object[0]);
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
                throw new ValueSourceResolutionError("Script threw an exception: " + e.getMessage());
                }

            context.raiseEvent(ValueSourceResolvedEventType.create(getDescription(), result));
            return result;
            }
        else
            {
            throw new ValueSourceResolutionError("Cannot execute the script because this WebDriver does not implement JavascriptExecutor.");
            }
        }

    private final MuseValueSource _script;
    private final MuseValueSource _arguments;

    public final static String ARGUMENTS_PARAM = "arguments";

    public final static String TYPE_ID = EvaluateJavascriptSource.class.getAnnotation(MuseTypeId.class).value();

    public static class StringExpressionSupport extends BaseArgumentedValueSourceStringSupport
        {
        @Override
        public String getName()
            {
            return "evaluateJavascriptInPage";
            }

        @Override
        protected int getNumberArguments()
            {
            return 1;
            }

        @Override
        protected boolean storeSingleArgumentAsSingleSubsource()
            {
            return true;
            }

        @Override
        protected String getTypeId()
            {
            return EvaluateJavascriptSource.TYPE_ID;
            }
        }
    }

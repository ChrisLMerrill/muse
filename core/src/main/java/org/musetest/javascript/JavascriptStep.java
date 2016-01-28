package org.musetest.javascript;

import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import javax.script.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptStep extends BaseStep
    {
    public JavascriptStep(StepConfiguration config, ResourceOrigin origin, String script)
        {
        super(config);
        _origin = origin;
        _script = script;
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context)
        {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.put("STEP_COMPLETE", new BasicStepExecutionResult(StepExecutionStatus.COMPLETE));
        engine.put("STEP_FAILURE", new BasicStepExecutionResult(StepExecutionStatus.FAILURE));
        engine.put("STEP_INCOMPLETE", new BasicStepExecutionResult(StepExecutionStatus.INCOMPLETE));
        try
            {
            String loader = prepareScript();
            engine.eval(loader);
            Object result = ((Invocable)engine).invokeFunction(FUNCTION_NAME, context);
            if (result instanceof StepExecutionResult)
                return (StepExecutionResult) result;
            else
                context.getTestExecutionContext().raiseEvent(new ScriptFailureEvent("Script did not return a StepExecutionResult. Instead, it returned: " + result, "n/a", null));// TODO do something better than n/a
            }
        catch (Throwable t)
            {
            LOG.error("unable to execute script: ", t);
            context.getTestExecutionContext().raiseEvent(new ScriptFailureEvent("Script threw an exception: " + t.getMessage(), "n/a", t));  // TODO do something better than n/a
            }
        return new BasicStepExecutionResult(StepExecutionStatus.FAILURE);
        }

    private String prepareScript()
        {
        if (_origin instanceof FileResourceOrigin)
            {
            File script_location = ((FileResourceOrigin)_origin).getFile();
            if (!script_location.exists())
                throw new IllegalArgumentException("File not found: " + script_location.getAbsolutePath());

            String path = script_location.getAbsolutePath();
            path = path.replace("\\", "\\\\");  // needed only for Windows
            return "load('" + path + "');"; // loading the script this way allows for interactive debugging in an IDE
            }
        else
            {
            return _script;
            }
        }

    private ResourceOrigin _origin;
    private String _script;

    public final static String FUNCTION_NAME = "executeStep";
    final static Logger LOG = LoggerFactory.getLogger(JavascriptStep.class);
    }



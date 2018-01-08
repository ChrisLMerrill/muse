package org.musetest.javascript;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.musetest.javascript.support.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptStep extends BaseStep
    {
    public JavascriptStep(StepConfiguration config, ResourceOrigin origin)
        {
        super(config);
        _origin = origin;
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        StepConfiguration config = getConfiguration();
        HashMap<String, Object> values = new HashMap<>();
        for (String name : config.getSourceNames())
            {
            MuseValueSource source = getValueSource(config, name, false, context.getProject());
            values.put(name, getValue(source, context, true, Object.class));
            }

        try
            {
            JavascriptRunner runner = new JavascriptStepRunner();
            runner.evalScript(_origin);

            Object result = runner.invokeFunction(EXECUTE_FUNCTION, context, values);
            if (result instanceof StepExecutionResult)
                return (StepExecutionResult) result;
            else
                context.raiseEvent(ScriptFailureEventType.create("Script did not return a StepExecutionResult. Instead, it returned: " + result, null));
            }
        catch (Throwable t)
            {
            LOG.error("unable to execute script: ", t);
            context.raiseEvent(ScriptFailureEventType.create("Script threw an exception: " + t.getMessage(), t));
            }
        return new BasicStepExecutionResult(StepExecutionStatus.FAILURE);
        }

    private ResourceOrigin _origin;

    public final static String EXECUTE_FUNCTION = "executeStep";
    private final static Logger LOG = LoggerFactory.getLogger(JavascriptStep.class);
    }

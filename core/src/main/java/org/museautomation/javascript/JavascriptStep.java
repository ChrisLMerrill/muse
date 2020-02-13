package org.museautomation.javascript;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.test.*;
import org.museautomation.javascript.support.*;
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

package org.museautomation.javascript;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.test.*;
import org.museautomation.core.values.*;
import org.museautomation.javascript.support.*;
import org.slf4j.*;

import javax.script.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptTest extends BaseMuseTest
    {
    public JavascriptTest(ResourceOrigin origin)
        {
        _origin = origin;
        }

    @Override
    protected boolean executeImplementation(TestExecutionContext context)
        {
        JavascriptRunner runner = new JavascriptRunner();
        runner.getScriptEngine().put("TEST_SUCCESS", null);
        runner.getScriptEngine().put("TEST_FAILURE", TestResult.create(getDescription(), getId(), "javascript implementation reported failure", TestResult.FailureType.Failure, "javascript implementation reported failure"));
        runner.getScriptEngine().put("TEST_ERROR", TestResult.create(getDescription(), getId(), "javascript implementation reported failure", TestResult.FailureType.Error, "javascript implementation reported error"));
        context.raiseEvent(StartTestEventType.create(getId(), getDescription()));
        Boolean result = true;
        try
            {
            runner.evalScript(_origin);
            Object result_obj = ((Invocable)runner.getScriptEngine()).invokeFunction(FUNCTION_NAME, context);
            if (result_obj != null)
	            {
	            context.raiseEvent(ScriptFailureEventType.create(result_obj.toString(), null));
	            result = false;
	            }
            }
        catch (Throwable t)
            {
            LOG.error("Script threw an exception", t);
            result = false;
            context.raiseEvent(TestErrorEventType.create("Script threw an exception: " + t.getMessage()));
            }
        context.raiseEvent(EndTestEventType.create());
        return result;
        }

    @Override
    public Map<String, ValueSourceConfiguration> getDefaultVariables()
        {
        return null;
        }

    @Override
    public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
        {

        }

    @Override
    public void setDefaultVariable(String name, ValueSourceConfiguration source)
        {

        }

    private ResourceOrigin _origin;

    public final static String FUNCTION_NAME = "executeTest";
    private final static Logger LOG = LoggerFactory.getLogger(JavascriptTest.class);
    }

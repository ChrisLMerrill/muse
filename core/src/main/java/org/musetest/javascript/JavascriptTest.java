package org.musetest.javascript;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.test.*;
import org.musetest.javascript.support.*;
import org.slf4j.*;

import javax.script.*;

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
    protected MuseTestResult executeImplementation(TestExecutionContext context)
        {
        JavascriptRunner runner = new JavascriptRunner();
        runner.getScriptEngine().put("TEST_SUCCESS", new BaseMuseTestResult(MuseTestResultStatus.Success));
        runner.getScriptEngine().put("TEST_FAILURE", new BaseMuseTestResult(MuseTestResultStatus.Failure));
        runner.getScriptEngine().put("TEST_ERROR", new BaseMuseTestResult(MuseTestResultStatus.Error));
        try
            {
            runner.evalScript(_origin);
            Object result = ((Invocable)runner.getScriptEngine()).invokeFunction(FUNCTION_NAME, context);
            if (result instanceof MuseTestResult)
                return (MuseTestResult) result;
            else
                context.raiseEvent(new ScriptFailureEvent("Script did not return a MuseTestResult. Instead, it returned: " + result, null));
            }
        catch (Throwable t)
            {
            LOG.error("unable to execute script: ", t);
            context.raiseEvent(new ScriptFailureEvent("Script threw an exception: " + t.getMessage(), t));
            }
        return new BaseMuseTestResult(MuseTestResultStatus.Error);
        }

    private ResourceOrigin _origin;

    public final static String FUNCTION_NAME = "executeTest";
    final static Logger LOG = LoggerFactory.getLogger(JavascriptTest.class);
    }



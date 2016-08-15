package org.musetest.javascript;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.test.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;
import org.musetest.javascript.support.*;
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
    protected MuseTestResult executeImplementation(TestExecutionContext context)
        {
        JavascriptRunner runner = new JavascriptRunner();
        runner.getScriptEngine().put("TEST_SUCCESS", null);
        runner.getScriptEngine().put("TEST_FAILURE", new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, "javascript implementation reported failure"));
        runner.getScriptEngine().put("TEST_ERROR", new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "javascript implementation reported error"));
        try
            {
            runner.evalScript(_origin);
            Object result = ((Invocable)runner.getScriptEngine()).invokeFunction(FUNCTION_NAME, context);
            if (result == null || result instanceof MuseTestFailureDescription)
                return new BaseMuseTestResult(this, new EventLog(), (MuseTestFailureDescription) result);
            else
                context.raiseEvent(new ScriptFailureEvent("Script did not return a MuseTestFailureDescription. Instead, it returned: " + result, null));
            }
        catch (Throwable t)
            {
            LOG.error("unable to execute script: ", t);
            context.raiseEvent(new ScriptFailureEvent("Script threw an exception: " + t.getMessage(), t));
            }
        return new BaseMuseTestResult(this, new EventLog(), new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "javascript implementation did not indicate success"));
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



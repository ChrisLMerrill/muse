package org.musetest.javascript;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import javax.script.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptTest extends BaseMuseTest
    {
    public JavascriptTest(ResourceOrigin origin, String script)
        {
        _origin = origin;
        _script = script;
        }


    @Override
    protected MuseTestResult executeImplementation(TestExecutionContext context)
        {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        engine.put("TEST_SUCCESS", new BaseMuseTestResult(MuseTestResultStatus.Success));
        engine.put("TEST_FAILURE", new BaseMuseTestResult(MuseTestResultStatus.Failure));
        engine.put("TEST_ERROR", new BaseMuseTestResult(MuseTestResultStatus.Error));
        try
            {
            String loader = prepareScript();
            engine.eval(loader);
            Object result = ((Invocable)engine).invokeFunction(FUNCTION_NAME, context);
            if (result instanceof MuseTestResult)
                return (MuseTestResult) result;
            else
                context.raiseEvent(new ScriptFailureEvent("Script did not return a MuseTestResult. Instead, it returned: " + result, _script, null));
            }
        catch (Throwable t)
            {
            LOG.error("unable to execute script: ", t);
            context.raiseEvent(new ScriptFailureEvent("Script threw an exception: " + t.getMessage(), _script, t));
            }
        return new BaseMuseTestResult(MuseTestResultStatus.Error);
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
            return "load('" + path + "')"; // loading the script this way allows for interactive debugging in an IDE
            }
        else
            {
            return _script;
            }
        }

    private ResourceOrigin _origin;
    private String _script;

    public final static String FUNCTION_NAME = "executeTest";
    final static Logger LOG = LoggerFactory.getLogger(JavascriptTest.class);
    }



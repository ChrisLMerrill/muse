package org.musetest.javascript.support;

import org.apache.commons.io.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptRunner
    {
    public void setVariable(String name, Object value)
        {
        _engine.put(name, value);
        }

    public Object evalScript(ResourceOrigin origin) throws ScriptException, IOException
        {
        try (InputStream input = origin.asStream())
            {
            String script = IOUtils.toString(input);
            return _engine.eval(script);
            }
        }

    public Object invokeFunction(String name, StepExecutionContext context, HashMap<String, Object> values) throws ScriptException, NoSuchMethodException
        {
        return ((Invocable)_engine).invokeFunction(name, values);
        }

    public ScriptEngine getScriptEngine()
        {
        return _engine;
        }

    private ScriptEngine _engine = new ScriptEngineManager().getEngineByName("javascript");
    }



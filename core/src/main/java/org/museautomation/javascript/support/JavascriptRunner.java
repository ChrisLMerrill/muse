package org.museautomation.javascript.support;

import org.apache.commons.io.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;

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
        if (origin instanceof FileResourceOrigin)
            return evalScript(((FileResourceOrigin)origin).getFile());

        try (InputStream input = origin.asInputStream())
            {
            return evalScript(input);
            }
        }

    public Object evalScript(InputStream input) throws ScriptException, IOException
        {
        String script = IOUtils.toString(input);
        return _engine.eval(script);
        }

    public Object evalScript(File file) throws ScriptException
        {
        String location = file.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\");
        String to_eval = String.format("load('%s');", location);
        return _engine.eval(to_eval);
        }

    public Object evalScript(String script) throws ScriptException
        {
        return _engine.eval(script);
        }

    public Object invokeFunction(String name, StepExecutionContext context, HashMap<String, Object> values) throws ScriptException, NoSuchMethodException
        {
        return ((Invocable)_engine).invokeFunction(name, context, values);
        }

    public ScriptEngine getScriptEngine()
        {
        return _engine;
        }

    private ScriptEngine _engine = new ScriptEngineManager().getEngineByName("javascript");
    }



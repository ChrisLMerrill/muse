package org.musetest.javascript.support;

import org.musetest.core.context.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptStepRunner extends JavascriptRunner
    {
    public JavascriptStepRunner() throws ScriptException, IOException
        {
        evalScript(getClass().getResourceAsStream("JavascriptStepRunnerSupport.js"));
        }

    @Override
    public Object invokeFunction(String name, StepExecutionContext context, HashMap<String, Object> values) throws ScriptException, NoSuchMethodException
        {
        getScriptEngine().put("__context", context);
        return super.invokeFunction(name, context, values);
        }
    }



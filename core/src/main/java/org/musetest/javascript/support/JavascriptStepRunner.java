package org.musetest.javascript.support;

import javax.script.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptStepRunner extends JavascriptRunner
    {
    public JavascriptStepRunner() throws ScriptException, IOException
        {
        evalScript(getClass().getResourceAsStream("JavascriptStepRunnerSupport.js"));
        }
    }



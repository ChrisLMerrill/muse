package org.musetest.selenium.mocks;

import org.openqa.selenium.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScriptableMockDriver extends MuseMockDriver implements JavascriptExecutor
    {
    @Override
    public Object executeScript(String script, Object... args)
        {
        if (script.contains(THROW_EXCEPTION))
            throw new RuntimeException("the script threw an exception");
        return null;
        }

    @Override
    public Object executeAsyncScript(String script, Object... args)
        {
        return null;
        }

    public final static String THROW_EXCEPTION = "exception";
    }



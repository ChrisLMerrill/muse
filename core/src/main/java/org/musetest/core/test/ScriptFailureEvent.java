package org.musetest.core.test;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScriptFailureEvent extends MuseEvent
    {
    public ScriptFailureEvent(String message, String script, Throwable exception)
        {
        super(MuseEventType.ScriptError);
        _message = message;
        _script = script;
        _exception = exception;
        }

    private String _message;
    private String _script;
    private Throwable _exception;
    }



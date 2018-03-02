package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.test.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class ThreadedTestRunner extends SimpleTestRunner implements Runnable
    {
    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public ThreadedTestRunner(MuseExecutionContext context, TestConfiguration config)
	    {
	    super(context, config);
	    _loader = context.getProject().getClassloader();
	    }

    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public ThreadedTestRunner(TestExecutionContext context)
        {
        super(context);
        _loader = context.getProject().getClassloader();
        }

    @Override
    public void runTest()
        {
        _thread = new Thread(this);
        if (_loader != null)
            _thread.setContextClassLoader(_loader);
        _thread.start();
        }

    public void interrupt()
        {
        _thread.interrupt();
        }

    @Override
    public void run()
        {
        super.runTest();
        }

    ClassLoader _loader;
    protected Thread _thread;
    }

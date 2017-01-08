package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class ThreadedTestRunner extends SimpleTestRunner implements Runnable
    {
    @SuppressWarnings("WeakerAccess")  // part of public API
    public ThreadedTestRunner(MuseProject project, MuseTest test, TestExecutionContext context)
        {
        super(project, test, context);
        if (project == null)
            _loader = getClass().getClassLoader();
        else
            _loader = project.getClassloader();
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
    private Thread _thread;
    }



package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.task.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class ThreadedTaskRunner extends SimpleTaskRunner implements Runnable
    {
    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public ThreadedTaskRunner(MuseExecutionContext context, TaskConfiguration config)
	    {
	    super(context, config);
	    _loader = context.getProject().getClassloader();
	    }

    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public ThreadedTaskRunner(TaskExecutionContext context)
        {
        super(context);
        _loader = context.getProject().getClassloader();
        }

    @Override
    public void runTask()
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
        super.runTask();
        }

    ClassLoader _loader;
    protected Thread _thread;
    }

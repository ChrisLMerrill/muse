package org.museautomation.core.execution;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.task.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // part of public API
public class BlockingThreadedTaskRunner extends ThreadedTaskRunner
    {
    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public BlockingThreadedTaskRunner(MuseExecutionContext context, TaskConfiguration task)
        {
        super(context, task);
        }

    @SuppressWarnings("unused,WeakerAccess")  // part of public API
    public BlockingThreadedTaskRunner(TaskExecutionContext context)
        {
        super(context);
        }

    @Override
    public void runTask()
        {
        super.runTask();
        try
            {
            _thread.join();
            }
        catch (InterruptedException e)
            {
            _task_context.raiseEvent(new MuseEvent(InterruptedEventType.TYPE));
            }
        }
    }



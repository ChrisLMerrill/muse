package org.museautomation.core.steptask;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.execution.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // public API
public class SteppedTaskExecutor
    {
    public SteppedTaskExecutor(SteppedTask task, SteppedTaskExecutionContext context)
        {
        _context = context;
        _task = task;
        _step_executor = new StepExecutor(task, context);
        }

    @SuppressWarnings("WeakerAccess")  // allow external usage of this API
    public boolean executeAll()
        {
        try
            {
            _step_executor.executeAll();
            }
        catch (Exception e)
            {
            //noinspection ConstantConditions
            if (e instanceof InterruptedException)
	            {
	            _context.raiseEvent(new MuseEvent(InterruptedEventType.TYPE));
	            return false;
	            }
            else
                throw e;
            }

        return true;
        }

    @SuppressWarnings("unused") // used in GUI
    public boolean executeNextStep()
        {
        return _step_executor.executeNextStep();
        }

    @SuppressWarnings("unused") // used in GUI
    public StepConfiguration getNextStep()
        {
        return _step_executor.getNextStep();
        }

    @SuppressWarnings("WeakerAccess,unused")  // used in GUI
    public void requestTerminate()
        {
        _step_executor.requestTerminate();
        }

    @SuppressWarnings("unused") // used in GUI
    public boolean terminateRequested()
        {
        return _step_executor.isTerminateRequested();
        }

    private SteppedTaskExecutionContext _context;
    private SteppedTask _task;
    private StepExecutor _step_executor;
    }
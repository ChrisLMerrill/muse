package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // public API
public class SteppedTestExecutor
    {
    public SteppedTestExecutor(SteppedTest test, SteppedTestExecutionContext context)
        {
        _context = context;
        _test = test;
        _step_executor = new StepExecutor(test, context);
        }

    @SuppressWarnings("WeakerAccess")  // allow external usage of this API
    public boolean executeAll()
        {
//        _context.raiseEvent(StartTestEventType.create(_test, _test.getDescription()));

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

//        _context.raiseEvent(EndTestEventType.create());
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

    private SteppedTestExecutionContext _context;
    private SteppedTest _test;
    private StepExecutor _step_executor;
    }
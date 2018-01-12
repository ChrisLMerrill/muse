package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;

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

        _resulter = new TestFailsOnErrorFailureOrInterrupt(test, context);  // should this be a test plugin?
        _context.addEventListener(_resulter);
        }

    @SuppressWarnings("WeakerAccess")  // allow external usage of this API
    public MuseTestResult executeAll()
        {
        _context.raiseEvent(StartTestEventType.create(_test));

        try
            {
            _step_executor.executeAll();
            }
        catch (Exception e)
            {
            //noinspection ConstantConditions
            if (e instanceof InterruptedException)
                _context.raiseEvent(new MuseEvent(InterruptedEventType.TYPE));
            else
                throw e;
            }

        final MuseTestResult result = _resulter.getTestResult();
        _context.raiseEvent(EndTestEventType.create(result.getOneLineDescription(), result.isPass()));
        return result;
        }

	public MuseTestResult getResult()
		{
		return _resulter.getTestResult();
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
    private TestResultProducer _resulter;
    }
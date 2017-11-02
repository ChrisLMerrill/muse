package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;
import org.slf4j.*;

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
        _resulter = new TestFailsOnErrorFailureOrInterrupt(test, context);

        _context.addEventListener(_resulter);
        }

    @SuppressWarnings("WeakerAccess")  // allow external usage of this API
    public MuseTestResult executeAll()
        {
        if (!startTest())
            finishTest();

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
        finishTest();
        return getResult();
        }

    public boolean startTest()
        {
        try
            {
            _context.runInitializers();
            }
        catch (MuseExecutionError e)
            {
            LOG.error("Unable to initialize the context", e);
            _context.raiseEvent(new TestErrorEvent("Unable to initialize the context"));
            return false;
            }

        _context.raiseEvent(new StartTestEvent(_test));
        return true;
        }

	public MuseTestResult getResult()
		{
		return _resulter.getTestResult();
		}

    public void finishTest()
        {
		final MuseTestResult result = _resulter.getTestResult();
		_context.raiseEvent(new EndTestEvent(result.getOneLineDescription(), result.isPass()));
        try
            {
            _context.cleanup();
            }
        catch (Throwable e)
            {
            LOG.error("Exception during test cleanup:", e);
            // regardless of exception here, we want to return the result.
            }
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

    private final static Logger LOG = LoggerFactory.getLogger(SteppedTestExecutor.class);
    }



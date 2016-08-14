package org.musetest.core.steptest;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.test.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SteppedTestExecutor
    {
    public SteppedTestExecutor(SteppedTest test, SteppedTestExecutionContext context)
        {
        _context = context;
        _test = test;
        _step_executor = new StepExecutor(test, context);
        _resulter = new TestFailsOnErrorFailureOrInterrupt(test, _step_executor.getEventLog());

        _context.addEventListener(_resulter);
        }

    @SuppressWarnings("WeakerAccess")  // allow external usage of this API
    public MuseTestResult executeAll()
        {
        if (!startTest())
            finishTest();

        _step_executor.executeAll();

        return finishTest();
        }

    public boolean startTest()
        {
        _context.raiseEvent(new StartTestEvent(_test));
        if (!_test.initializeContext(_context))
            {
            _context.raiseEvent(new TestErrorEvent("Unable to initialize the context"));
            return false;
            }

        return true;
        }

    public MuseTestResult finishTest()
        {
        MuseTestResult result = _resulter.getTestResult();
        _context.raiseEvent(new EndTestEvent(result));
        _context.cleanup();
        return result;
        }

    public boolean executeNextStep()
        {
        return _step_executor.executeNextStep();
        }

    public StepConfiguration getNextStep()
        {
        return _step_executor.getNextStep();
        }

    @SuppressWarnings("WeakerAccess,unused")  // used in GUI
    public void requestTerminate()
        {
        _step_executor.requestTerminate();
        }

    private SteppedTestExecutionContext _context;
    private SteppedTest _test;
    private StepExecutor _step_executor;
    private TestResultProducer _resulter;
    }



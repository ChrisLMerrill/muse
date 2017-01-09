package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.steptest.*;
import org.slf4j.*;

/**
 * Provides management of exeuction of a SteppedTest on a separate thread, with the ability
 * to run, pause and single-step the test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // public API
public class InteractiveTestRunner extends ThreadedTestRunner implements Runnable
    {
    public InteractiveTestRunner(MuseProject project, SteppedTest test, TestExecutionContext context)
        {
        super(project, test, context);
        }

    @Override
    public void run()
        {
        SteppedTestExecutionContext stepped_context;
        if (_context instanceof SteppedTestExecutionContext)
            stepped_context = (SteppedTestExecutionContext) _context;
        else
            stepped_context = new DefaultSteppedTestExecutionContext(_context);

        _executor = new SteppedTestExecutor((SteppedTest) _test, stepped_context);
        _executor.startTest();
        boolean has_more = true;
        while (!_interrupted && has_more && !_executor.terminateRequested())
            {
            if (!pause() || _step_requested)
                has_more = _executor.executeNextStep();
            _step_requested = false;
            }

        if (_interrupted)
            _context.raiseEvent(new MuseEvent(MuseEventType.Interrupted));

        setTestResult(_executor.finishTest());
        _thread = null;
        }

    private synchronized boolean pause()
        {
        if (!_pause_requested)
            return false;

        _paused = true;
        _context.raiseEvent(new PauseTestEvent(_executor.getNextStep()));

        try
            {
            wait();
            _paused = false;
            if (!_interrupted)
                _context.raiseEvent(new MuseEvent(MuseEventType.Resume));
            }
        catch (InterruptedException e)
            {
            _interrupted = true;
            }

        return true;
        }

    public synchronized void requestStop()
        {
        _interrupted = true;
        if (_paused)
            notify();
        else
            _thread.interrupt();
        }

    public void requestPause()
        {
        _pause_requested = true;
        }

    public synchronized void requestResume()
        {
        if (!_pause_requested)
            {
            LOG.error("cannot resume - runner is not paused");
            return;
            }

        _pause_requested = false;
        notify();
        }

    public synchronized void requestStep()
        {
        if (!_pause_requested)
            {
            LOG.error("cannot step - runner is not paused");
            return;
            }

        _step_requested = true;
        notify();
        }

    EventLog getEventLog()
        {
        return _executor.getEventLog();
        }

    private boolean _interrupted = false;
    private boolean _pause_requested = false;
    private boolean _step_requested = false;
    private boolean _paused = false;
    private SteppedTestExecutor _executor;
    private Thread _thread;

    private final static Logger LOG = LoggerFactory.getLogger(InteractiveTestRunner.class);
    }



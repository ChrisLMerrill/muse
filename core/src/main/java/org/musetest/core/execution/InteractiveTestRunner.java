package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.steptest.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

/**
 * Provides management of exeuction of a SteppedTest on a separate thread, with the ability
 * to run, pause and single-step the test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class InteractiveTestRunner extends ThreadedTestRunner implements Runnable
    {
    public InteractiveTestRunner(MuseProject project, SteppedTest test, TestExecutionContext context)
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
        Thread t = new Thread(this);
        t.setContextClassLoader(_loader);
        t.start();
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
        while (!_interrupted && has_more)
            {
            if (!paused() || _step_requested)
                has_more = _executor.executeNextStep();
            _step_requested = false;
            }

        if (_interrupted)
            _context.raiseEvent(new MuseEvent(MuseEventType.Interrupted));

        setTestResult(_executor.finishTest());
        }

    private synchronized boolean paused()
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
// TODO throw an event?
        _interrupted = true;
        if (_paused)
            notify();
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

    private ClassLoader _loader;
    private boolean _interrupted = false;
    private boolean _pause_requested = false;
    private boolean _step_requested = false;
    private boolean _paused = false;
    private SteppedTestExecutor _executor;

    final static Logger LOG = LoggerFactory.getLogger(InteractiveTestRunner.class);
    }



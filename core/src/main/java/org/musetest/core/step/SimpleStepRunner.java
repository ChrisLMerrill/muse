package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleStepRunner
    {
    public SimpleStepRunner(StepConfiguration step) throws MuseExecutionError
        {
        this(step, new SimpleProject());
        }

    public SimpleStepRunner(StepConfiguration step, MuseProject project) throws MuseExecutionError
        {
        _step = step;
        _project = project;
        setup();
        }

    public SimpleStepRunner(StepConfiguration step, MuseExecutionContext context) throws MuseExecutionError
        {
        _step = step;
        _context = context;
        _project = context.getProject();
        setup();
        }

    private void setup() throws MuseExecutionError
        {
        _test = new SteppedTest(_step);
        if (_context != null)
            _test_context = new DefaultSteppedTestExecutionContext(_context, _test);
        else
            _test_context = new DefaultSteppedTestExecutionContext(_project, _test);
        _test_context.initializePlugins();
        _executor = new StepExecutor(_test, _test_context);
        }

    public SimpleStepRunner runOneStep()
        {
        _executor.executeNextStep();
        return this;
        }

    public SimpleStepRunner runAll()
        {
        _executor.executeAll();
        return this;
        }

    public MuseExecutionContext context()
        {
        return _test_context;
        }

    public EventLog eventLog()
        {
        return _test_context.getEventLog();
        }

    public StepExecutor executor()
        {
        return _executor;
        }

    private StepConfiguration _step;
    private MuseProject _project;
    private SteppedTest _test;
    private MuseExecutionContext _context;
    private SteppedTestExecutionContext _test_context;
    private StepExecutor _executor;
    }

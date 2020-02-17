package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.project.*;
import org.museautomation.core.steptask.*;

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
        _task = new SteppedTask(_step);
        if (_context != null)
            _test_context = new DefaultSteppedTaskExecutionContext(_context, _task);
        else
            _test_context = new DefaultSteppedTaskExecutionContext(_project, _task);
        _test_context.initializePlugins();
        _executor = new StepExecutor(_task, _test_context);
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
    private SteppedTask _task;
    private MuseExecutionContext _context;
    private SteppedTaskExecutionContext _test_context;
    private StepExecutor _executor;
    }

package org.musetest.core.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DummyStepExecutionContext implements StepExecutionContext
    {
    public DummyStepExecutionContext()
        {
        }

    public DummyStepExecutionContext(MuseProject project)
        {
        _test_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(project));
        }

    @Override
    public SteppedTestExecutionContext getTestExecutionContext()
        {
        return _test_context;
        }

    private SteppedTestExecutionContext _test_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext());

    @Override
    public StepConfiguration getCurrentStepConfiguration()
        {
        return null;
        }

    @Override
    public MuseStep getCurrentStep() throws MuseInstantiationException
        {
        return null;
        }

    @Override
    public void stepComplete(MuseStep step, StepExecutionResult result)
        {
        }

    @Override
    public boolean hasStepToExecute()
        {
        return false;
        }

    @Override
    public Object getLocalVariable(String name)
        {
        return _variables.get(name);
        }

    @Override
    public void setLocalVariable(String name, Object value)
        {
        _variables.put(name, value);
        }

    @Override
    public Map<String, Object> getVariables()
        {
        return _variables;
        }

    @Override
    public void raiseEvent(MuseEvent event)
        {
        _test_context.raiseEvent(event);
        }

    @Override
    public MuseProject getProject()
        {
        return _test_context.getProject();
        }

    @Override
    public StepExecutionContextStack getExecutionStack()
        {
        return _test_context.getExecutionStack();
        }

    @Override
    public void registerShuttable(Shuttable shuttable)
        {
        _test_context.registerShuttable(shuttable);
        }

    private Map<String, Object> _variables = new HashMap<>();
    }



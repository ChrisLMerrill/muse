package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseStepExecutionContext implements StepExecutionContext
    {
    public BaseStepExecutionContext(SteppedTestExecutionContext test_context, boolean new_variable_scope)
        {
        _test_context = test_context;
        if (new_variable_scope)
            _step_vars = new HashMap<>();
        }

    @Override
    public SteppedTestExecutionContext getTestExecutionContext()
        {
        return _test_context;
        }

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
        return _test_context.getLocalVariable(name);
        }

    @Override
    public void setLocalVariable(String name, Object value)
        {
        _test_context.setLocalVariable(name, value);
        }

    @Override
    public Map<String, Object> getVariables()
        {
        return _step_vars;
        }

    @Override
    public StepExecutionContextStack getExecutionStack()
        {
        return _test_context.getExecutionStack();
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
    public void registerShuttable(Shuttable shuttable)
        {
        _test_context.registerShuttable(shuttable);
        }

    @Override
    public SteppedTestExecutionContext getParent()
        {
        return _test_context;
        }

    @Override
    public void removeEventListener(MuseEventListener listener)
        {
        _test_context.removeEventListener(listener);
        }

    @Override
    public void addEventListener(MuseEventListener listener)
        {
        _test_context.addEventListener(listener);
        }

    private SteppedTestExecutionContext _test_context;
    private Map<String, Object> _step_vars = null;
    }



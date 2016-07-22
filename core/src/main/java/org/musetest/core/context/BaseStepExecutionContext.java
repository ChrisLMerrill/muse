package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;

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

    private SteppedTestExecutionContext _test_context;
    private Map<String, Object> _step_vars = null;
    }



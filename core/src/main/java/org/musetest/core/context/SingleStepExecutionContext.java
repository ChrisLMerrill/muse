package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SingleStepExecutionContext extends BaseStepExecutionContext
    {
    public SingleStepExecutionContext(SteppedTestExecutionContext test_context, StepConfiguration config, boolean new_variable_scope)
        {
        super(test_context, new_variable_scope);
        _config = config;
        }

    @Override
    public StepConfiguration getCurrentStepConfiguration()
        {
        return _config;
        }

    @Override
    public MuseStep getCurrentStep() throws MuseInstantiationException
        {
        if (_current_step == null)
            _current_step = _config.createStep(getTestExecutionContext().getProject());
        return _current_step;
        }

    @Override
    public void stepComplete(MuseStep step, StepExecutionResult result)
        {
        if (!result.getStatus().equals(StepExecutionStatus.ERROR))
            getTestExecutionContext().getExecutionStack().pop();
        }

    @Override
    public boolean hasStepToExecute()
        {
        return true;
        }

    private MuseStep _current_step;
    private StepConfiguration _config;
    }



package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SingleStepExecutionContext extends BaseStepExecutionContext
    {
    public SingleStepExecutionContext(StepsExecutionContext test_context, StepConfiguration config, boolean new_variable_scope)
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
            _current_step = _config.createStep(getProject());
        return _current_step;
        }

    @Override
    public void stepComplete(MuseStep step, StepExecutionResult result)
        {
        if (result.getStatus().equals(StepExecutionStatus.ERROR))
            _current_step = null;   // re-create next time (for interactive debugging)
        else
            getExecutionStack().pop();
        }

    @Override
    public boolean hasStepToExecute()
        {
        return true;
        }

    private MuseStep _current_step;
    private StepConfiguration _config;
    }



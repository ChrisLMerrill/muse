package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ListOfStepsExecutionContext extends BaseStepExecutionContext
    {
    public ListOfStepsExecutionContext(StepsExecutionContext test_context, List<StepConfiguration> steps, boolean new_variable_scope, ListOfStepsCompletionListener listener)
        {
        super(test_context, new_variable_scope);
        _steps = steps;
        _listener = listener;
        }

    @Override
    public StepConfiguration getCurrentStepConfiguration()
        {
        try
            {
            return _steps.get(_current_index);
            }
        catch (IndexOutOfBoundsException e)
            {
            return null;
            }
        }

    @Override
    public MuseStep getCurrentStep() throws MuseInstantiationException
        {
        if (_current_step == null)
            _current_step = _steps.get(_current_index).createStep(getProject());
        return _current_step;
        }

    @Override
    public void stepComplete(MuseStep step, StepExecutionResult result)
        {
        if (result.getStatus().equals(StepExecutionStatus.ERROR))
            _current_step = null;  // re-create the step next time (for interactive debugging)
        else if (!result.getStatus().equals(StepExecutionStatus.ERROR) && _current_step != null)
            {
            _current_step = null;
            _current_index++;
            if (_current_index >= _steps.size())
                {
                _finished = true;
                _listener.stepsComplete();
                }
            }
        }

    @Override
    public boolean hasStepToExecute()
        {
        return !_finished;
        }

    private boolean _finished = false;
    private int _current_index = 0;
    private MuseStep _current_step;
    private List<StepConfiguration> _steps;
    private ListOfStepsCompletionListener _listener;
    }



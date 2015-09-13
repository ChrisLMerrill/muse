package org.musetest.core.step;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BasicStepExecutionResult implements StepExecutionResult
    {
    public BasicStepExecutionResult(StepExecutionStatus status)
        {
        _status = status;
        }

    public BasicStepExecutionResult(StepExecutionStatus status, String description)
        {
        _status = status;
        _description = description;
        }

    @Override
    public StepExecutionStatus getStatus()
        {
        return _status;
        }

    @Override
    public String getDescription()
        {
        if (_description != null)
            return _description;
        return _status.name();
        }

    private StepExecutionStatus _status;
    private String _description;
    }



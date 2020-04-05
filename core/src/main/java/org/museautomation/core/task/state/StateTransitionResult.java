package org.museautomation.core.task.state;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionResult
    {
    public StateTransitionResult(String failure_message)
        {
        _success = false;
        _failure_message = failure_message;
        }

    public StateTransitionResult(TaskResult task_result, InterTaskState output_state)
        {
        _success = true;
        _task_result = task_result;
        _output_state = output_state;
        }

    public InterTaskState outputState()
        {
        return _output_state;
        }

    public TaskResult taskResult()
        {
        return _task_result;
        }

    public boolean transitionSuccess()
        {
        return _success;
        }

    public String getFailureMessage()
        {
        return _failure_message;
        }

    private boolean _success;
    private String _failure_message;
    private TaskResult _task_result = null;
    private InterTaskState _output_state = null;
    }
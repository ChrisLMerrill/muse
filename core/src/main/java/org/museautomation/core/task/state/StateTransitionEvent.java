package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionEvent
    {
    public StateTransitionEvent(StateTransitionContext context)
        {
        _context = context;
        }

    public StateTransitionContext getContext()
        {
        return _context;
        }

    private StateTransitionContext _context;

    public static class StartTransitionEvent extends StateTransitionEvent
        {
        public StartTransitionEvent(StateTransitionContext context)
            {
            super(context);
            }
        }

    public static class StartTransitionTaskEvent extends StateTransitionEvent
        {
        public StartTransitionTaskEvent(StateTransitionContext context, TaskExecutionContext task_context)
            {
            super(context);
            _task_context = task_context;
            }

        public TaskExecutionContext getTaskContext()
            {
            return _task_context;
            }

        private TaskExecutionContext _task_context;
        }

    public static class EndTransitionTaskEvent extends StateTransitionEvent
        {
        public EndTransitionTaskEvent(StateTransitionContext context, TaskResult result)
            {
            super(context);
            _result = result;
            }

        public TaskResult getResult()
            {
            return _result;
            }

        private TaskResult _result;
        }

    public static class EndTransitionEvent extends StateTransitionEvent
        {
        public EndTransitionEvent(StateTransitionContext context, StateTransitionResult result)
            {
            super(context);
            _result = result;
            }

        public StateTransitionResult getResult()
            {
            return _result;
            }

        private StateTransitionResult _result;
        }
    }
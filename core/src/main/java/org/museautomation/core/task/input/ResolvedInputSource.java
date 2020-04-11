package org.museautomation.core.task.input;

import org.museautomation.core.task.state.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ResolvedInputSource
    {
    public abstract String getDescription();

    public static class TaskStateSource extends ResolvedInputSource
        {
        public TaskStateSource(InterTaskState state)
            {
            _state = state;
            }

        @Override
        public String getDescription()
            {
            return "from task state " + _state.getStateDefinitionId();
            }

        public InterTaskState getState()
            {
            return _state;
            }

        private final InterTaskState _state;
        }

    public static class InputProviderSource extends ResolvedInputSource
        {
        public InputProviderSource(TaskInputProvider provider)
            {
            _provider = provider;
            }

        @Override
        public String getDescription()
            {
            return "from task input provider " + _provider.getDescription();
            }

        public TaskInputProvider getProvider()
            {
            return _provider;
            }

        private final TaskInputProvider _provider;
        }

    public static class DefaultValueSource extends ResolvedInputSource
        {
        @Override
        public String getDescription()
            {
            return "from default value ";
            }
        }
    }
package org.museautomation.core.task.state;

import org.museautomation.core.context.*;
import org.museautomation.core.project.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionContext extends DelegatesToParentExecutionContext
    {
    public StateTransitionContext(StateTransitionConfiguration transition_config, InterTaskState input_state, StateContainer container, SimpleProject project)
        {
        super(new ProjectExecutionContext(project), null);
        _transition_config = transition_config;
        _container = container;
        _input_state = input_state;
        }

    public InterTaskState getInputState()
        {
        return _input_state;
        }

    public StateTransitionConfiguration getConfig()
        {
        return _transition_config;
        }

    public StateContainer getContainer()
        {
        return _container;
        }

    private StateTransitionConfiguration _transition_config;
    private InterTaskState _input_state;
    private StateContainer _container;
    }
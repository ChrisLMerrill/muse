package org.museautomation.core.task.state;

import org.museautomation.core.context.*;
import org.museautomation.core.project.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionContext extends DelegatesToParentExecutionContext
    {
    public StateTransitionContext(StateTransitionConfiguration transition_config, StateContainer container, SimpleProject project)
        {
        super(new ProjectExecutionContext(project), null);
        _transition_config = transition_config;
        _container = container;
        }

    public StateTransitionConfiguration getConfig()
        {
        return _transition_config;
        }

    public StateContainer getContainer()
        {
        return _container;
        }

    public void addTransitionListener(StateTransitionListener listener)
        {
        _listeners.add(listener);
        }
    public void removeTransitionListener(StateTransitionListener listener)
        {
        _listeners.remove(listener);
        }

    public void raiseTransitionEvent(StateTransitionEvent event)
        {
        for (StateTransitionListener listener : _listeners)
            listener.transitionEvent(event);
        }

    private final StateTransitionConfiguration _transition_config;
    private final StateContainer _container;
    private final Set<StateTransitionListener> _listeners = new HashSet<>();
    }
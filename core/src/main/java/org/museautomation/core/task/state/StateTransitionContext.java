package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.task.input.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionContext extends DelegatesToParentExecutionContext
    {
    public StateTransitionContext(StateTransitionConfiguration transition_config, StateContainer container, MuseProject project)
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

    public void setResult(StateTransitionResult result)
        {
        _result = result;
        }

    public StateTransitionResult getResult()
        {
        return _result;
        }

    public List<TaskInputProvider> getInputProviders()
        {
        return Collections.unmodifiableList(_providers);
        }

    public void addInputProvider(TaskInputProvider provider)
        {
        _providers.add(provider);
        }

    private final StateTransitionConfiguration _transition_config;
    private final StateContainer _container;
    private StateTransitionResult _result = null;
    private final List<TaskInputProvider> _providers = new ArrayList<>();
    }
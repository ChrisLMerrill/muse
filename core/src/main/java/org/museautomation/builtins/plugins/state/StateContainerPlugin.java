package org.museautomation.builtins.plugins.state;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.state.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateContainerPlugin implements MusePlugin
    {
    public StateContainerPlugin(StateContainer container)
        {
        _container = container;
        }

    @Override
    public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
        {
        return true;
        }

    @Override
    public void initialize(MuseExecutionContext context)
        {

        }

    public void addState(InterTaskState state)
        {
        _container.addState(state);
        }

    public StateContainer getContainer()
        {
        return _container;
        }

    @Override
    public void shutdown()
        {

        }

    @Override
    public String getId()
        {
        return "state-container-plugin";
        }

    private StateContainer _container = new BasicStateContainer();
    }
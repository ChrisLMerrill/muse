package org.museautomation.builtins.plugins.state;

import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.state.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateStorePlugin implements MusePlugin, StateStore
    {
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
        _states.add(state);
        }

    public List<InterTaskState> findStates(String type_id)
        {
        List<InterTaskState> matches = new ArrayList<>();
        for (InterTaskState state : _states)
            if (type_id.equals(state.getStateDefinitionId()))
                matches.add(state);
        return matches;
        }

    @Override
    public void shutdown()
        {

        }

    @Override
    public String getId()
        {
        return "state-provider-plugin";
        }

    private Set<InterTaskState> _states = new HashSet<>();
    }
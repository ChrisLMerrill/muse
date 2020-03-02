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
        _store.addState(state);
        }

    public List<InterTaskState> findStates(String type_id)
        {
        return _store.findStates(type_id);
        }

    public SimpleStateStore getStore()
        {
        return _store;
        }

    public void setStore(SimpleStateStore store)
        {
        _store = store;
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

    private SimpleStateStore _store = new SimpleStateStore();
    }
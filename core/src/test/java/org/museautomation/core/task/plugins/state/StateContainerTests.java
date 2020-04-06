package org.museautomation.core.task.plugins.state;

import org.junit.jupiter.api.*;
import org.museautomation.core.task.state.*;

import javax.annotation.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateContainerTests
    {
    @Test
    public void addState()
        {
        _container.addState(_state1);
        Assertions.assertTrue(_states_added.contains(_state1));
        Assertions.assertTrue(_states_replaced.isEmpty());
        Assertions.assertTrue(_container.contains(_state1));

        _container.addState(_state2);
        Assertions.assertTrue(_states_added.contains(_state2));
        }

    @Test
    public void removeState()
        {
        _container.addState(_state1);

        _container.removeState(_state1);
        Assertions.assertTrue(_states_removed.contains(_state1));
        Assertions.assertTrue(_states_replaced.isEmpty());
        Assertions.assertFalse(_container.contains(_state1));

        _container.addState(_state1);
        _container.addState(_state2);

        _states_removed.clear();
        _container.removeState(_state1);
        Assertions.assertTrue(_states_removed.contains(_state1));
        Assertions.assertFalse(_container.contains(_state1));
        Assertions.assertTrue(_container.contains(_state2));
        }

    @Test
    public void replaceState()
        {
        _container.addState(_state2);

        _states_added.clear();
        _states_replaced.clear();
        _container.replaceState(_state2, _state1);
        Assertions.assertTrue(_states_removed.isEmpty());
        Assertions.assertTrue(_states_added.isEmpty());
        Assertions.assertEquals(_state1, _states_replaced.get(_state2));
        Assertions.assertTrue(_container.contains(_state1));
        Assertions.assertFalse(_container.contains(_state2));
        }

    @Test
    public void listenerRemoved()
        {
        _container.removeStateListener(_listener);
        _container.addState(_state1);
        _container.replaceState(_state1, _state2);
        _container.removeState(_state2);

        Assertions.assertTrue(_states_added.isEmpty());
        Assertions.assertTrue(_states_removed.isEmpty());
        Assertions.assertTrue(_states_replaced.isEmpty());
        }


    @BeforeEach
    public void setup()
        {
        _state1.setStateDefinitionId("state1");
        _state2.setStateDefinitionId("state2");
        _container.addStateListener(_listener);
        }

    private InterTaskState _state1 = new InterTaskState();
    private InterTaskState _state2 = new InterTaskState();
    private StateContainer _container = new BasicStateContainer();
    private List<InterTaskState> _states_added = new ArrayList<>();
    private List<InterTaskState> _states_removed = new ArrayList<>();
    private Map<InterTaskState, InterTaskState> _states_replaced = new HashMap<>();

    StateContainer.StateContainerChangeListener _listener = new StateContainer.StateContainerChangeListener()
        {
        @Override
        public void stateAdded(@Nonnull InterTaskState state)
            {
            _states_added.add(state);
            }

        @Override
        public void stateRemoved(@Nonnull InterTaskState state)
            {
            _states_removed.add(state);
            }

        @Override
        public void stateReplaced(@Nonnull InterTaskState old_state, @Nonnull InterTaskState new_state)
            {
            _states_replaced.put(old_state, new_state);
            }
        };
    }
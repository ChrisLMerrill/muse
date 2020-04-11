package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("state-transition")
public class StateTransitionConfiguration extends GenericResourceConfiguration
    {
    public String getTaskId()
        {
        return _task_id;
        }

    public void setTaskId(String task_id)
        {
        _task_id = task_id;
        }

    public TransitionInputState getInputState()
        {
        return _input_state;
        }

    public void setInputState(TransitionInputState input_state)
        {
        _input_state = input_state;
        }

    public List<TransitionOutputState> getOutputStates()
        {
        return _output_states;
        }

    public void setOutputStates(List<TransitionOutputState> output_states)
        {
        if (output_states == null)
            throw new IllegalArgumentException("Null value not allowed");
        _output_states = output_states;
        }

    public void addOutputState(TransitionOutputState output_state)
        {
        _output_states.add(output_state);
        }

    private String _task_id;
    private TransitionInputState _input_state;
    private List<TransitionOutputState> _output_states = new ArrayList<>();

    @Override
    public ResourceType getType()
        {
        return new StateTransitionResourceType();
        }

    public final static String TYPE_ID = StateTransitionConfiguration.class.getAnnotation(MuseTypeId.class).value();

    static class StateTransitionResourceType extends ResourceType
        {
        public StateTransitionResourceType()
            {
            super(TYPE_ID, "State Transition", StateTransition.class);
            }

        @Override
        public MuseResource create()
            {
            return new StateTransitionConfiguration();
            }
        }

    public static class TransitionInputState
        {
        // required for de/serialization
        @SuppressWarnings("unused")
        public TransitionInputState()
            {
            }

        public TransitionInputState(String state_id)
            {
            _state_id = state_id;
            }

        public String getStateId()
            {
            return _state_id;
            }

        // required for de/serialization
        @SuppressWarnings("unused")
        public void setStateId(String state_id)
            {
            _state_id = state_id;
            }

        public boolean isTerminate()
            {
            return _terminate;
            }

        public void setTerminate(boolean terminate)
            {
            _terminate = terminate;
            }

        private String _state_id;
        private boolean _terminate = false;
        }

    public static class TransitionOutputState
        {
        // required for de/serialization
        @SuppressWarnings("unused")
        public TransitionOutputState()
            {
            }

        public TransitionOutputState(String state_id)
            {
            _state_id = state_id;
            }

        public String getStateId()
            {
            return _state_id;
            }

        // required for de/serialization
        @SuppressWarnings("unused")
        public void setStateId(String state_id)
            {
            _state_id = state_id;
            }

        public boolean isReplacesInput()
            {
            return _replaces_input;
            }

        public boolean isRequired()
            {
            return _required;
            }

        public void setReplacesInput(boolean replaces_input)
            {
            _replaces_input = replaces_input;
            }

        // required for de/serialization
        @SuppressWarnings("unused")
        public boolean isSkipIncomplete()
            {
            return _skip_incomplete;
            }

        public void setSkipIncomplete(boolean skipIncomplete)
            {
            _skip_incomplete = skipIncomplete;
            }

        public void setRequired(boolean required)
            {
            _required = required;
            }

        private String _state_id;
        private boolean _replaces_input = false;
        private boolean _required = true;
        private boolean _skip_incomplete = false;
        }
    }
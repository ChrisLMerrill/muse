package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.resource.generic.*;
import org.museautomation.core.resource.types.*;

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

    public TransitionOutputState getOutputState()
        {
        return _output_state;
        }

    public void setOutputState(TransitionOutputState output_state)
        {
        _output_state = output_state;
        }

    private String _task_id;
    private TransitionInputState _input_state;
    private TransitionOutputState _output_state;

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

        public void setStateId(String state_id)
            {
            _state_id = state_id;
            }

        private String _state_id;
        }

    public static class TransitionOutputState
        {
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

        public void setStateId(String state_id)
            {
            _state_id = state_id;
            }

        private String _state_id;
        }
    }
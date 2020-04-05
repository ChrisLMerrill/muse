package org.museautomation.core.task.plugins.state;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.plugins.input.*;
import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.state.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionTests
    {
    @Test
    public void basicTransition()
        {
        StateTransition transition = new StateTransition(_trans_context);
        StateTransitionResult result = transition.execute();

        Assertions.assertTrue(result.transitionSuccess());
        Assertions.assertTrue(result.taskResult().isPass());
        Assertions.assertEquals(_output_state_def.getId(), result.outputState().getStateDefinitionId());
        Assertions.assertEquals(6L, result.outputState().getValues().get("out-val"));
        Assertions.assertTrue(_container.contains(_input_state));
        Assertions.assertTrue(_container.contains(result.outputState()));
        }

    @Test
    public void badTaskId() throws IOException
        {
        MuseResource resource = new Function();
        resource.setId(_transition_config.getTaskId());
        ResourceStorage storage = _trans_context.getProject().getResourceStorage();
        storage.removeResource(storage.findResource(_transition_config.getTaskId()));
        storage.addResource(resource);

        StateTransition transition = new StateTransition(_trans_context);
        StateTransitionResult result = transition.execute();

        Assertions.assertFalse(result.transitionSuccess());
        Assertions.assertNotNull(result.getFailureMessage());
        }

    @Test
    public void missingTaskId()
        {
        _transition_config.setTaskId("missing-id");
        StateTransition transition = new StateTransition(_trans_context);
        StateTransitionResult result = transition.execute();

        Assertions.assertFalse(result.transitionSuccess());
        Assertions.assertNotNull(result.getFailureMessage());
        }


    @Test
    public void replaceState()  // output state replaces the input state
        {
        StateTransition transition = new StateTransition(_trans_context);
        // TODO _transition_config.   <-- configure output state to replace the input state, instead of adding beside it
        StateTransitionResult result = transition.execute();

        Assertions.assertFalse(_container.contains(_input_state));
        Assertions.assertTrue(_container.contains(result.outputState()));
        }

    @Test
    public void terminateState()  // no output state, input state is terminated (removed)
        {
        StateTransition transition = new StateTransition(_trans_context);
        // TODO _transition_config.   <-- configure output state to replace the input state, instead of adding beside it
        transition.execute();

        Assertions.assertEquals(0, _container.size());
        }

    @Test
    public void noOutputState()
        {
        _transition_config.setOutputState(null);
        StateTransition transition = new StateTransition(_trans_context);
        StateTransitionResult result = transition.execute();

        Assertions.assertTrue(_container.contains(_input_state));
        Assertions.assertEquals(1, _container.size());
        Assertions.assertNull(result.outputState());
        }


    @Test
    public void transitionWithPlugin() throws IOException
        {
        ResourceStorage storage = _trans_context.getProject().getResourceStorage();
        storage.removeResource(storage.findResource(_transition_config.getTaskId()));
        // create Task with required input and output
        _task = new MockTask()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                context.raiseEvent(StartTaskEventType.create(getId(), "mock"));
                context.outputs().storeOutput("out-val", (Long) context.getVariable("in-val")  + (Long) context.getVariable("in-val2"));
                context.raiseEvent(EndTaskEventType.create());
                return true;
                }
            };
        _task.setId(_transition_config.getTaskId());
        _task.getInputSet().addInput(new TaskInput("in-val", new IntegerValueType().getId(), true));
        _task.getInputSet().addInput(new TaskInput("in-val2", new IntegerValueType().getId(), true));
        _trans_context.getProject().getResourceStorage().addResource(_task);

        StateTransition transition = new StateTransition(_trans_context);
        InputProvider provider = new InputProvider()
            {
            @Override
            public boolean isLastChanceProvider() { return false; }
            @Override
            public Map<String, Object> gatherInputValues(TaskInputSet inputs, MuseExecutionContext context)
                {
                HashMap<String, Object> map = new HashMap<>();
                map.put("in-val2", 13L);
                return map;
                }
            };
        transition.addPlugin(new InputProviderPlugin(provider));
        transition.addPlugin(new InjectInputsPlugin(new InjectInputsPluginConfiguration()));
        StateTransitionResult result = transition.execute();

        Assertions.assertEquals(16L, result.outputState().getValues().get("out-val"));
        }

    @Test
    public void transitionToAlternateState()
        {
        // TODO test a task that can transition to more than one state (e.g. depending on input)
        Assertions.fail("ToDo");
        }

    @Test
    public void fillOutputStateFromInputState()
        {
        // TODO carry value from the input to the output, without the task adding them to output
        Assertions.fail("ToDo");
        }


    @Test
    public void transitionWithInputProvider() throws IOException
        {
        ResourceStorage storage = _trans_context.getProject().getResourceStorage();
        storage.removeResource(storage.findResource(_transition_config.getTaskId()));
        // create Task with required input and output
        _task = new MockTask()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                context.raiseEvent(StartTaskEventType.create(getId(), "mock"));
                context.outputs().storeOutput("out-val", (Long) context.getVariable("in-val")  + (Long) context.getVariable("in-val2"));
                context.raiseEvent(EndTaskEventType.create());
                return true;
                }
            };
        _task.setId(_transition_config.getTaskId());
        _task.getInputSet().addInput(new TaskInput("in-val", new IntegerValueType().getId(), true));
        _task.getInputSet().addInput(new TaskInput("in-val2", new IntegerValueType().getId(), true));
        _trans_context.getProject().getResourceStorage().addResource(_task);

        StateTransition transition = new StateTransition(_trans_context);
        InputProvider provider = new InputProvider()
            {
            @Override
            public boolean isLastChanceProvider() { return false; }
            @Override
            public Map<String, Object> gatherInputValues(TaskInputSet inputs, MuseExecutionContext context)
                {
                HashMap<String, Object> map = new HashMap<>();
                map.put("in-val2", 13L);
                return map;
                }
            };
        transition.addInputProvider(provider);
        StateTransitionResult result = transition.execute();

        Assertions.assertEquals(16L, result.outputState().getValues().get("out-val"));
        }

    @BeforeEach
    public void setup() throws IOException
        {
        SimpleProject project = new SimpleProject();

        // create input State Definitions
        String in_state_type = "in-state";
        _input_state_def = new StateDefinition();
        List<StateValueDefinition> in_states = new ArrayList<>();
        in_states.add(new StateValueDefinition("in-val", new IntegerValueType(), true));
        _input_state_def.setValues(in_states);
        _input_state_def.setId(in_state_type);
        project.getResourceStorage().addResource(_input_state_def);

        // create output State Definitions
        String out_state_type = "out-state";
        _output_state_def = new StateDefinition();
        List<StateValueDefinition> out_states = new ArrayList<>();
        out_states.add(new StateValueDefinition("out-val", new IntegerValueType(), true));
        _output_state_def.setValues(out_states);
        _output_state_def.setId(out_state_type);
        project.getResourceStorage().addResource(_output_state_def);

        // create Task with required input and output
        _task = new MockTask()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                context.raiseEvent(StartTaskEventType.create(getId(), "mock"));
                context.outputs().storeOutput("out-val", (Long) context.getVariable("in-val") * 2);
                context.raiseEvent(EndTaskEventType.create());
                return true;
                }
            };
        _task.setId("task1");
        _task.getInputSet().addInput(new TaskInput("in-val", new IntegerValueType().getId(), true));
        project.getResourceStorage().addResource(_task);

        // create starting state
        _input_state = new InterTaskState();
        _input_state.setStateDefinitionId(_input_state_def.getId());
        _input_state.setValue("in-val", 3L);

        // create the transition
        _transition_config = new StateTransitionConfiguration();
        _transition_config.setInputState(new StateTransitionConfiguration.TransitionInputState(_input_state_def.getId()));
        _transition_config.setOutputState(new StateTransitionConfiguration.TransitionOutputState(_output_state_def.getId()));
        _transition_config.setTaskId(_task.getId());

        // create the transition context
        _trans_context = new StateTransitionContext(_transition_config, _input_state, _container, project);
        _container.addState(_input_state);
        }

    private MuseTask _task;
    private InterTaskState _input_state;
    private StateDefinition _input_state_def;
    private StateDefinition _output_state_def;
    private StateTransitionConfiguration _transition_config;
    private StateTransitionContext _trans_context;
    private StateContainer _container = new BasicStateContainer();
    }
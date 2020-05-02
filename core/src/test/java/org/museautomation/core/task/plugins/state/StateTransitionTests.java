package org.museautomation.core.task.plugins.state;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.plugins.results.*;
import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.input.*;
import org.museautomation.core.task.state.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StateTransitionTests
    {
    @Test
    public void basicTransition()
        {
        StateTransitionResult result = _transition.execute();

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

        StateTransitionResult result = _transition.execute();

        Assertions.assertFalse(result.transitionSuccess());
        Assertions.assertNotNull(result.getFailureMessage());
        }

    @Test
    public void missingTaskId()
        {
        _transition_config.setTaskId("missing-id");
        StateTransitionResult result = _transition.execute();

        Assertions.assertFalse(result.transitionSuccess());
        Assertions.assertNotNull(result.getFailureMessage());
        }


    @Test
    public void replaceState()  // output state replaces the input state
        {
        _transition_config.getOutputStates().get(0).setReplacesInput(true);
        StateTransitionResult result = _transition.execute();

        Assertions.assertFalse(_container.contains(_input_state));
        Assertions.assertTrue(_container.contains(result.outputState()));
        }

    @Test
    public void terminateState()  // no output state, input state is terminated (removed)
        {
        _transition_config.setOutputStates(new ArrayList<>());
        _transition_config.getInputState().setTerminate(true);
        _transition.execute();

        Assertions.assertEquals(0, _container.size());
        }

    @Test
    public void noOutputState()
        {
        _transition_config.setOutputStates(new ArrayList<>());
        StateTransitionResult result = _transition.execute();

        Assertions.assertTrue(_container.contains(_input_state));
        Assertions.assertEquals(1, _container.size());
        Assertions.assertNull(result.outputState());
        }

    @Test
    public void incompleteOutputState()  // generate an error if the output state is incomplete
        {
        _output_state_def.add(new StateValueDefinition("out-val2", new IntegerValueType(), true));
        StateTransitionResult result = _transition.execute();
        Assertions.assertFalse(result.transitionSuccess());
        Assertions.assertTrue(result.getFailureMessage().contains("out-state"));
        }

    @Test
    public void resultsInFirstCompleteOutputState() throws IOException  // the first complete state becomes the output
        {
        _output_state_def.add(new StateValueDefinition("out-val2", new IntegerValueType(), true));
        _transition_config.getOutputStates().get(0).setSkipIncomplete(true);
        _transition_config.getOutputStates().get(0).setRequired(false);
        StateDefinition second_output_state = new StateDefinition();
        second_output_state.setId("second_outstate");
        second_output_state.add(new StateValueDefinition("out-val", new IntegerValueType(), true));
        _trans_context.getProject().getResourceStorage().addResource(second_output_state);
        _transition_config.addOutputState(new StateTransitionConfiguration.TransitionOutputState(second_output_state.getId()));

        StateTransitionResult result = _transition.execute();
        Assertions.assertTrue(result.transitionSuccess());
        Assertions.assertEquals("second_outstate", result.outputState().getStateDefinitionId());
        Assertions.assertEquals(6L, result.outputState().getValues().get("out-val"));
        }

    @Test
    public void fillOutputStateFromInputState()
        {
        _input_state_def.add(new StateValueDefinition("other_val", new StringValueType(), true));
        _input_state.getValues().put("other_val", "carried along");
        _output_state_def.add(new StateValueDefinition("other_val", new StringValueType(), true));
        StateTransitionResult result = _transition.execute();

        Assertions.assertEquals("carried along", result.outputState().getValues().get("other_val"));
        }

    @Test
    public void emptyInputState()
        {
        _transition_config.setInputState(null);
        _input_state = null;
        _input_state_def = null;

        TaskInputProvider provider = new SingleTaskInputProvider()
            {
            @Override
            public Object resolveInput(TaskInputResolutionResults resolved, TaskInput input, MuseExecutionContext context)
                {
                if (input.getName().equals("in-val"))
                    return 13L;
                return null;
                }

            @Override
            public String getDescription()
                {
                return "provider";
                }
            };
        _trans_context.addInputProvider(provider);

        StateTransitionResult result = _transition.execute();

        Assertions.assertEquals(26L, result.outputState().getValues().get("out-val"));
        }

    @Test
    public void taskFailure() throws IOException
        {
        _task = new MockTask()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                context.raiseEvent(StartTaskEventType.create(getId(), "mock"));
                context.raiseEvent(MessageEventType.createError("task failure"));
                context.raiseEvent(EndTaskEventType.create());
                return true;
                }
            };
        replaceTask(_task);

        StateTransitionResult result = _transition.execute();
        Assertions.assertFalse(result.transitionSuccess());
        Assertions.assertTrue(result.getFailureMessage().contains("task failure"));
        }

    private void replaceTask(MuseTask task) throws IOException
        {
        ResourceStorage storage = _trans_context.getProject().getResourceStorage();
        storage.removeResource(storage.findResource(_transition_config.getTaskId()));
        task.setId(_transition_config.getTaskId());
        _trans_context.getProject().getResourceStorage().addResource(task);
        }

    @Test
    public void transitionWithInputProvider() throws IOException
        {
        // create Task with required input and output
        _task = new MockTask()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                context.raiseEvent(StartTaskEventType.create(getId(), "mock"));
                //noinspection RedundantCast
                context.outputs().storeOutput("out-val", (Long) context.getVariable("in-val")  + (Long) context.getVariable("in-val2"));
                context.raiseEvent(EndTaskEventType.create());
                return true;
                }
            };
        _task.getInputSet().addInput(new TaskInput("in-val", new IntegerValueType().getId(), true));
        _task.getInputSet().addInput(new TaskInput("in-val2", new IntegerValueType().getId(), true));
        replaceTask(_task);

        TaskInputProvider provider = new SingleTaskInputProvider()
            {
            @Override
            public Object resolveInput(TaskInputResolutionResults resolved, TaskInput input, MuseExecutionContext context)
                {
                if (input.getName().equals("in-val2"))
                    return 13L;
                return null;
                }

            @Override
            public String getDescription()
                {
                return "provider";
                }
            };
        _trans_context.addInputProvider(provider);
        StateTransitionResult result = _transition.execute();

        Assertions.assertEquals(16L, result.outputState().getValues().get("out-val"));
        }

    @Test
    public void eventsReceived()
        {
        AtomicReference<MuseEvent> start_transition_event = new AtomicReference<>();
        AtomicReference<MuseEvent> end_transition_event = new AtomicReference<>();
        AtomicReference<MuseEvent> start_task_event = new AtomicReference<>();
        AtomicReference<MuseEvent> end_task_event = new AtomicReference<>();
        AtomicReference<MuseEvent> start_inputs_event = new AtomicReference<>();
        AtomicReference<MuseEvent> end_inputs_event = new AtomicReference<>();
        _trans_context.addEventListener(event ->
            {
            if (StartStateTransitionEventType.TYPE_ID.equals(event.getTypeId()))
                start_transition_event.set(event);
            else if (EndStateTransitionEventType.TYPE_ID.equals(event.getTypeId()))
                end_transition_event.set(event);
            else if (StartTaskEventType.TYPE_ID.equals(event.getTypeId()))
                start_task_event.set(event);
            else if (EndTaskEventType.TYPE_ID.equals(event.getTypeId()))
                end_task_event.set(event);
            else if (StartResolvingTransitionInputsEventType.TYPE_ID.equals(event.getTypeId()))
                start_inputs_event.set(event);
            else if (EndResolvingTransitionInputsEventType.TYPE_ID.equals(event.getTypeId()))
                end_inputs_event.set(event);
            });

        StateTransitionResult result = _transition.execute();

        Assertions.assertNotNull(start_transition_event.get());
        Assertions.assertNotNull(end_transition_event.get());
        Assertions.assertNotNull(start_task_event.get());
        Assertions.assertNotNull(end_task_event.get());
        Assertions.assertNotNull(start_inputs_event.get());
        Assertions.assertNotNull(end_inputs_event.get());
        Assertions.assertEquals(result, _trans_context.getResult());
        Assertions.assertTrue(_trans_context.getResult().taskResult().isPass());
        }

    @Test
    public void inputStateNotFound()
        {
        _container.removeState(_input_state);
        StateTransitionResult result = _transition.execute();
        Assertions.assertFalse(result.transitionSuccess());
        }

    @Test
    public void missingInputs()
        {
        _input_state.setValues(new HashMap<>());
        StateTransitionResult result = _transition.execute();
        Assertions.assertFalse(result.transitionSuccess());
        }


    @BeforeEach
    public void setup() throws IOException
        {
        SimpleProject project = new SimpleProject();
        project.getResourceStorage().addResource(new TaskResultCollectorConfiguration.TaskResultCollectorConfigurationType().create());

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
        _output_state_def.add(new StateValueDefinition("out-val", new IntegerValueType(), true));
        _output_state_def.setId(out_state_type);
        project.getResourceStorage().addResource(_output_state_def);

        // create Task with required input and output
        _task = new MockTask()
            {
            @Override
            protected boolean executeImplementation(TaskExecutionContext context)
                {
                context.outputs().storeOutput("out-val", (Long) context.getVariable("in-val") * 2);
                return true;
                }
            };
        _task.setId("task1");
        _task.getInputSet().addInput(new TaskInput("in-val", new IntegerValueType().getId(), true));
        _task.getOutputSet().addOutput(new TaskOutput("out-val", new IntegerValueType().getId()));
        project.getResourceStorage().addResource(_task);

        // create starting state
        _input_state = new InterTaskState();
        _input_state.setStateDefinitionId(_input_state_def.getId());
        _input_state.setValue("in-val", 3L);

        // create the transition
        _transition_config = new StateTransitionConfiguration();
        _transition_config.setInputState(new StateTransitionConfiguration.TransitionInputState(_input_state_def.getId()));
        _transition_config.addOutputState(new StateTransitionConfiguration.TransitionOutputState(_output_state_def.getId()));
        _transition_config.setTaskId(_task.getId());

        // create the transition context
        _trans_context = new StateTransitionContext(_transition_config, _container, project);
        _trans_context.addPlugin(new TaskResultCollectorConfiguration().createPlugin());  // this need to be added for our test projects
        _container.addState(_input_state);

        // finally, create the transition
        _transition = new StateTransition(_trans_context);
        }

    private MuseTask _task;
    private StateDefinition _input_state_def;
    private StateDefinition _output_state_def;
    private InterTaskState _input_state;
    private StateTransitionConfiguration _transition_config;
    private StateTransitionContext _trans_context;
    private final StateContainer _container = new BasicStateContainer();
    private StateTransition _transition;
    }
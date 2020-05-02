package org.museautomation.core.task.plugins.input;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.input.*;
import org.museautomation.core.task.state.*;
import org.museautomation.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskInputResolutionTests
    {
    @Test
    public void resolveInputsFromState()
        {
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, _input_state).execute();

        Assertions.assertTrue(results.inputsSatisfied(_task));
        Assertions.assertEquals(3L, results.getResolvedInput("in-val").getValue());
        ResolvedInputSource.TaskStateSource source = (ResolvedInputSource.TaskStateSource) results.getResolvedInput("in-val").getSource();
        Assertions.assertSame(_input_state, source.getState());
        Assertions.assertTrue(source.getDescription().contains(_input_state.getStateDefinitionId()));
        }

    // also tests null input state
    @Test
    public void resolveInputsFromDefaults()
        {
        _task.getInputSet().getInput("in-val").setDefault(ValueSourceConfiguration.forValue(5L));
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, InterTaskState.START).execute();

        Assertions.assertTrue(results.inputsSatisfied(_task));
        Assertions.assertEquals(5L, results.getResolvedInput("in-val").getValue());
        Assertions.assertTrue(results.getResolvedInput("in-val").getSource() instanceof ResolvedInputSource.DefaultValueSource);
        }

    @Test
    public void resolveInputsFromProviders()
        {
        TaskInputProvider input_provider = new SingleTaskInputProvider()
            {
            @Override
            public String getDescription()
                {
                return "test";
                }

            @Override
            public Object resolveInput(TaskInputResolutionResults resolved, TaskInput input, MuseExecutionContext context)
                {
                return 3L;
                }
            };
        _trans_context.addInputProvider(input_provider);
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, InterTaskState.START).execute();
        Assertions.assertTrue(results.inputsSatisfied(_task));
        Assertions.assertEquals(3L, results.getResolvedInput("in-val").getValue());
        ResolvedInputSource.InputProviderSource source = (ResolvedInputSource.InputProviderSource) results.getResolvedInput("in-val").getSource();
        Assertions.assertSame(input_provider, source.getProvider());
        Assertions.assertTrue(source.getDescription().contains("test"));
        }

    // input provider returns the wrong data type...it is ignored (logged)
    @Test
    public void rejectWrongTypeInputFromProvider()
        {
        TaskInputProvider input_provider = new SingleTaskInputProvider()
            {
            @Override
            public String getDescription()
                {
                return "test";
                }

            @Override
            public Object resolveInput(TaskInputResolutionResults resolved, TaskInput input, MuseExecutionContext context)
                {
                return "some text";
                }
            };
        _trans_context.addInputProvider(input_provider);
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, InterTaskState.START).execute();
        Assertions.assertFalse(results.inputsSatisfied(_task));
        Assertions.assertNull(results.getResolvedInput("in-val"));
        }

    @Test
    public void missingInputs()
        {
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, InterTaskState.START).execute();
        Assertions.assertFalse(results.inputsSatisfied(_task));
        Assertions.assertNull(results.getResolvedInput("in-val"));
        }

    @Test
    public void missingOptionalInputsAreAllowed()
        {
        _task.getInputSet().getInput("in-val").setRequired(false);
        _task.getInputSet().getInput("in-val").setDefault(null);
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, InterTaskState.START).execute();
        Assertions.assertNull(results.getResolvedInput("in-val"));
        Assertions.assertTrue(results.inputsSatisfied(_task));
        }

    /**
     * This tests the case where one input cannot be resolved until others are resolved. In this case, multiple
     * iterations of the resolution process may be required, depending on the order involved. Typically, this is
     * an input provider that is dependent on another input provider (that is behind it in the list) or on a
     * default value (which is resolved after input providers).
     */
    @Test
    public void inputProviderResolvesAfterDefaultsResolved()
        {
        TaskInputProvider input_provider = new SingleTaskInputProvider()
            {
            @Override
            public String getDescription()
                {
                return "test";
                }

            @Override
            public Object resolveInput(TaskInputResolutionResults resolved, TaskInput input, MuseExecutionContext context)
                {
                if (resolved.getResolvedInput("dependent-val") != null)
                    return 3L;
                return
                    null;
                }
            };
        _trans_context.addInputProvider(input_provider);
        _task.getInputSet().addInput(new TaskInput("dependent-val", new AnyValueType().getId(), ValueSourceConfiguration.forValue("stuff")));
        TaskInputResolutionResults results = new TaskInputResolution(_trans_context, _task_context, InterTaskState.START).execute();
        Assertions.assertEquals(3L, results.getResolvedInput("in-val").getValue());
        }


    @BeforeEach
    public void setup() throws IOException
        {
        SimpleProject project = new SimpleProject();

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

        // create task context
        _task_context = new DefaultTaskExecutionContext(project, _task);

        // create starting state
        _input_state = new InterTaskState();
        _input_state.setStateDefinitionId("input-state1");
        _input_state.setValue("in-val", 3L);

        // create the transition context
        _trans_context = new StateTransitionContext(new StateTransitionConfiguration(), new BasicStateContainer(), project);
        }

    private MuseTask _task;
    private TaskExecutionContext _task_context;
    private InterTaskState _input_state;
    private StateTransitionContext _trans_context;
    }
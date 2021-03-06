package org.museautomation.core.context;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.step.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.task.plugins.*;
import org.museautomation.core.tests.mocks.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.variables.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import static org.museautomation.core.tests.mocks.MockStepCreatesShuttable.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ExecutionContextTests
	{
	@Test
    void projectContextEvents()
		{
		checkReceiveEvents(new ProjectExecutionContext(_project));
		}

	@Test
    void testContextEvents()
		{
		final DefaultTaskExecutionContext context = new DefaultTaskExecutionContext(_project, new MockTask());

		checkReceiveEvents(context);
		checkParentEventPropogation(context, false);
		}

	@Test
    void steppedTestContextEvents()
		{
		final DefaultSteppedTaskExecutionContext context = new DefaultSteppedTaskExecutionContext(_project, new SteppedTask(new StepConfiguration(LogMessage.TYPE_ID)));

		checkReceiveEvents(context);
		checkParentEventPropogation(context, false);
		}

	@Test
    void singleStepContextEvents()
		{
		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
		StepsExecutionContext parent = new DefaultSteppedTaskExecutionContext(_project, new SteppedTask(step));
		final SingleStepExecutionContext context = new SingleStepExecutionContext(parent, step, true);

		checkReceiveEvents(context);
		checkParentEventPropogation(context, true);
		}

	@Test
    void listOfStepsContextReceivesEvents()
		{
		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
		StepsExecutionContext parent = new DefaultSteppedTaskExecutionContext(_project, new SteppedTask(step));
		List<StepConfiguration> steps = Collections.singletonList(step);
		final ListOfStepsExecutionContext context = new ListOfStepsExecutionContext(parent, steps, true, null);

		checkReceiveEvents(context);
		checkParentEventPropogation(context, true);
		}

	private void checkReceiveEvents(MuseExecutionContext context)
		{
		List<MuseEvent> events_received = new ArrayList<>();
		MuseEventListener listener = events_received::add;

		context.addEventListener(listener);
		context.raiseEvent(MessageEventType.create("message1"));

		Assertions.assertEquals(1, events_received.size());
		Assertions.assertEquals(MessageEventType.TYPE_ID, events_received.get(0).getTypeId());

		context.removeEventListener(listener);
		context.raiseEvent(MessageEventType.create("message2"));
		events_received.clear();
		Assertions.assertEquals(0, events_received.size());
		}

	private void checkParentEventPropogation(MuseExecutionContext child_context, boolean propogates_to_parent)
		{
		List<MuseEvent> events_received = new ArrayList<>();
		MuseEventListener listener = events_received::add;

		child_context.getParent().addEventListener(listener);
		child_context.raiseEvent(MessageEventType.create("message1"));

		Assertions.assertEquals(propogates_to_parent, 1 == events_received.size());

		child_context.getParent().removeEventListener(listener);
		}

	@Test
    void getProject()
		{
		ProjectExecutionContext context = new ProjectExecutionContext(_project);
        Assertions.assertSame(_project, context.getProject());

		// TODO test for other contexts
		}

	@Test
    void variableScoping()
		{
		// set same variable in multiple contexts to different values
		MuseExecutionContext project_context = new ProjectExecutionContext(_project);
		TaskSuiteExecutionContext suite_context = new DefaultTaskSuiteExecutionContext(project_context, new SimpleTaskSuite());
		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
		SteppedTaskExecutionContext test_context = new DefaultSteppedTaskExecutionContext(suite_context, new SteppedTask(step));
		SingleStepExecutionContext step_context = new SingleStepExecutionContext(test_context, step, true);

		// ensure the correct values are retrieved from each level
		final String varname = "var1";
		project_context.setVariable(varname, "projectval");
		suite_context.setVariable(varname, "suiteval");
		test_context.setVariable(varname, "testval");
		step_context.setVariable(varname, "stepval");
		step_context.setVariable("var2", "stepval-into-execution", ContextVariableScope.Execution);

		Assertions.assertEquals("projectval", project_context.getVariable(varname));
		Assertions.assertEquals("suiteval", suite_context.getVariable(varname));
		Assertions.assertEquals("testval", test_context.getVariable(varname));
		Assertions.assertEquals("stepval", step_context.getVariable(varname));
        Assertions.assertEquals("stepval-into-execution", test_context.getVariable("var2"));

		Assertions.assertEquals("projectval", step_context.getVariable(varname, VariableQueryScope.Project));
		Assertions.assertEquals("suiteval", step_context.getVariable(varname, VariableQueryScope.Suite));
		Assertions.assertEquals("testval", step_context.getVariable(varname, VariableQueryScope.Execution));
		Assertions.assertEquals("stepval", step_context.getVariable(varname, VariableQueryScope.Local));

        Assertions.assertNull(step_context.getVariable("var2", VariableQueryScope.Local));
		Assertions.assertEquals("stepval-into-execution", step_context.getVariable("var2", VariableQueryScope.Execution));
		}

    /**
     * Assures that variables set in a taks without a scoped group will be set into the execution context, rather than the local context.
     */
    @Test
    void variableScopingIntoExecution()
   		{
   		MuseExecutionContext project_context = new ProjectExecutionContext(_project);
   		TaskSuiteExecutionContext suite_context = new DefaultTaskSuiteExecutionContext(project_context, new SimpleTaskSuite());
   		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
   		SteppedTaskExecutionContext test_context = new DefaultSteppedTaskExecutionContext(suite_context, new SteppedTask(step));
   		ListOfStepsExecutionContext steps_context = new ListOfStepsExecutionContext(test_context, List.of(step), false, null);
   		SingleStepExecutionContext step_context = new SingleStepExecutionContext(steps_context, step, false);

   		// ensure the correct values are retrieved from each level
   		final String varname = "var1";
   		step_context.setVariable(varname, "stepval");

   		Assertions.assertNull(project_context.getVariable(varname));
   		Assertions.assertNull(suite_context.getVariable(varname));
   		Assertions.assertEquals("stepval", test_context.getVariable(varname));
   		Assertions.assertEquals("stepval", step_context.getVariable(varname));

        Assertions.assertEquals("stepval", test_context.getVariable(varname, VariableQueryScope.Execution));
        Assertions.assertNull(test_context.getVariable(varname, VariableQueryScope.Local));
   		}

	@Test
    void createVariable()
		{
		String varname = _context.createVariable("varprefix-", "value");
		Assertions.assertTrue(varname.startsWith("varprefix-"));
		Assertions.assertEquals("value", _context.getVariable(varname));
		}

	@Test
    void createVariableTwice()
		{
		String varname1 = _context.createVariable("varprefix-", "value1");
		String varname2 = _context.createVariable("varprefix-", "value2");
		Assertions.assertNotEquals(varname1, varname2);
		Assertions.assertEquals("value1", _context.getVariable(varname1));
		Assertions.assertEquals("value2", _context.getVariable(varname2));
		}

	@Test
    void shutShuttables()
		{
		// create a test with a step that creates a Shuttable resource
		MuseProject project = new SimpleProject();
		StepConfiguration step = new StepConfiguration(MockStepCreatesShuttable.TYPE_ID);
        SteppedTask task = new SteppedTask(step);

		// run the test
		final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(project, task);
	    TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
		Assertions.assertTrue(result.isPass());

		// verify the resource was created and closed
        Assertions.assertEquals(1, context.getEventLog().findEvents(event ->
        {
        final String description = EventTypes.DEFAULT.findType(event).getDescription(event);
        return description != null && description.contains(EXECUTE_MESSAGE);
        }).size(), "The step did not run");
		MockShuttable shuttable = (MockShuttable) context.getVariable(MockStepCreatesShuttable.SHUTTABLE_VAR_NAME);
		Assertions.assertNotNull(shuttable);
		Assertions.assertTrue(shuttable.isShutdown());
		}

	@Test
    void queueNewEventsDuringProcessing()
		{
		final StepConfiguration step_config = new StepConfiguration(LogMessage.TYPE_ID);
		DefaultTaskExecutionContext context = new DefaultTaskExecutionContext(new SimpleProject(), new SteppedTask(step_config));

		// install an event listener that raises an event in response to another event
		AtomicReference<MuseEvent> event2 = new AtomicReference<>(null);
		context.addEventListener(event ->
		{
		if (event.getTypeId().equals(MessageEventType.TYPE_ID))  // don't go into infinite loop
			{
			MuseEvent second_event = StepEventType.create(EndStepEventType.TYPE_ID, step_config);
			event2.set(second_event);
			context.raiseEvent(second_event);
			}
		});

		// install an event listener to track the event ordering
		final List<MuseEvent> events = new ArrayList<>();
		context.addEventListener(events::add);

		// raise an event
		final MuseEvent event1 = MessageEventType.create("message");
		context.raiseEvent(event1);

		Assertions.assertEquals(2, events.size());
        Assertions.assertSame(event1, events.get(0));
        Assertions.assertSame(event2.get(), events.get(1));
		}

	@Test
    void getTestExecutionId()
		{
		MuseTask test = new MockTask("test1");
		TaskExecutionContext context = new DefaultTaskExecutionContext(_context, test);
		String id = context.getTaskExecutionId();
		Assertions.assertNotNull(id);
		}

	@Test
    void getTestExecutionIdInSuite()
		{
		MuseTaskSuite suite = new SimpleTaskSuite();
		suite.setId("suite1");
		TaskSuiteExecutionContext suite_context = new DefaultTaskSuiteExecutionContext(_context, suite);
		MuseTask test = new MockTask("test1");
		TaskExecutionContext context = new DefaultTaskExecutionContext(suite_context, test);
		String id1 = context.getTaskExecutionId();
		Assertions.assertNotNull(id1);
		Assertions.assertTrue(context.getTaskExecutionId().length() >= test.getId().length());

		Assertions.assertNotEquals(id1, new DefaultTaskExecutionContext(_context, test).getTaskExecutionId()); // should be unique for each test context
		}

	@Test
    void successfulPluginInitEvent() throws MuseExecutionError
		{
		MockContext context = new MockContext();
		AtomicBoolean is_shutdown = new AtomicBoolean(false);
		MockTestPlugin success_plugin = new MockTestPlugin(true, true)
			{
			@Override
			public void shutdown()
				{
				is_shutdown.set(true);
				}
			};
		success_plugin._id = "success-plugin";

		context.addPlugin(success_plugin);
		int fail_count = context.initializePlugins();
		Assertions.assertEquals(0, fail_count);
		Assertions.assertEquals(1, context._events_raised.size());
		final MuseEvent event = context._events_raised.get(0);
		Assertions.assertEquals(PluginInitializedEventType.TYPE_ID, event.getTypeId());
		Assertions.assertFalse(event.hasTag(MuseEvent.ERROR));
		Assertions.assertFalse(event.hasTag(MuseEvent.FAILURE));
		Assertions.assertTrue(event.getDescription().contains(success_plugin.getId()));

		context.cleanup();
		Assertions.assertTrue(is_shutdown.get());
		}

	@Test
    void failedPluginInitEvent() throws MuseExecutionError
		{
		MockContext context = new MockContext();
		AtomicBoolean is_shutdown = new AtomicBoolean(false);
		MockTestPlugin fail_plugin = new MockTestPlugin(true, true)
			{
			@Override
			public void shutdown()
				{
				is_shutdown.set(true);
				}

			@Override
			public void initialize(MuseExecutionContext context) throws MuseExecutionError
				{
				throw new MuseExecutionError("failed");
				}
			};
		fail_plugin._id = "fail-plugin";

		context.addPlugin(fail_plugin);
		int fail_count = context.initializePlugins();
		Assertions.assertEquals(1, fail_count);
		Assertions.assertEquals(1, context._events_raised.size());
		final MuseEvent event = context._events_raised.get(0);
		Assertions.assertEquals(PluginInitializedEventType.TYPE_ID, event.getTypeId());
		Assertions.assertTrue(event.hasTag(MuseEvent.ERROR));
		Assertions.assertTrue(event.getDescription().contains(fail_plugin.getId()));
		Assertions.assertTrue(new PluginInitializedEventType().getDescription(event).contains("failed"));

		context.cleanup();
		Assertions.assertFalse(is_shutdown.get());  // failed plugins aren't shutdown()
		}

	@Test
    void pluginShutdownOrder() throws MuseExecutionError
		{
		MockContext context = new MockContext();
		List<String> shutdown_plugin_ids = new ArrayList<>();
		MockTestPlugin plugin1 = new MockTestPlugin(true, true)
			{
			@Override
			public void shutdown()
				{
				shutdown_plugin_ids.add(getId());
				}
			};
		plugin1._id = "plugin1";
		context.addPlugin(plugin1);

		MockTestPlugin plugin2 = new MockTestPlugin(true, true)
			{
			@Override
			public void shutdown()
				{
				shutdown_plugin_ids.add(getId());
				}
			};
		plugin2._id = "plugin2";
		context.addPlugin(plugin2);

		Assertions.assertEquals(0, context.initializePlugins());

		context.cleanup();
		Assertions.assertEquals(2, shutdown_plugin_ids.size());
		Assertions.assertEquals("plugin2", shutdown_plugin_ids.get(0));
		Assertions.assertEquals("plugin1", shutdown_plugin_ids.get(1));
		}

	@BeforeEach
    void setup()
		{
		_project = new SimpleProject();
		_context = new ProjectExecutionContext(_project);
		}

	private MuseExecutionContext _context;
	private MuseProject _project;
	}

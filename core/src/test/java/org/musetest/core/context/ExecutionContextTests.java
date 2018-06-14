package org.musetest.core.context;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.suite.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.tests.utils.*;
import org.musetest.core.variables.*;

import java.util.*;
import java.util.concurrent.atomic.*;

import static org.musetest.core.tests.mocks.MockStepCreatesShuttable.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExecutionContextTests
	{
	@Test
	public void projectContextEvents()
		{
		checkReceiveEvents(new ProjectExecutionContext(_project));
		}

	@Test
	public void testContextEvents()
		{
		final DefaultTestExecutionContext context = new DefaultTestExecutionContext(_project, new MockTest());

		checkReceiveEvents(context);
		checkParentEventPropogation(context, false);
		}

	@Test
	public void steppedTestContextEvents()
		{
		final DefaultSteppedTestExecutionContext context = new DefaultSteppedTestExecutionContext(_project, new SteppedTest(new StepConfiguration(LogMessage.TYPE_ID)));

		checkReceiveEvents(context);
		checkParentEventPropogation(context, false);
		}

	@Test
	public void singleStepContextEvents()
		{
		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
		StepsExecutionContext parent = new DefaultSteppedTestExecutionContext(_project, new SteppedTest(step));
		final SingleStepExecutionContext context = new SingleStepExecutionContext(parent, step, true);

		checkReceiveEvents(context);
		checkParentEventPropogation(context, true);
		}

	@Test
	public void listOfStepsContextReceivesEvents()
		{
		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
		StepsExecutionContext parent = new DefaultSteppedTestExecutionContext(_project, new SteppedTest(step));
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

		Assert.assertEquals(1, events_received.size());
		Assert.assertEquals(MessageEventType.TYPE_ID, events_received.get(0).getTypeId());

		context.removeEventListener(listener);
		context.raiseEvent(MessageEventType.create("message2"));
		events_received.clear();
		Assert.assertEquals(0, events_received.size());
		}

	private void checkParentEventPropogation(MuseExecutionContext child_context, boolean propogates_to_parent)
		{
		List<MuseEvent> events_received = new ArrayList<>();
		MuseEventListener listener = events_received::add;

		child_context.getParent().addEventListener(listener);
		child_context.raiseEvent(MessageEventType.create("message1"));

		Assert.assertEquals(propogates_to_parent, 1 == events_received.size());

		child_context.getParent().removeEventListener(listener);
		}

	@Test
	public void getProject()
		{
		ProjectExecutionContext context = new ProjectExecutionContext(_project);
		Assert.assertTrue(_project == context.getProject());

		// TODO test for other contexts
		}

	@Test
	public void variableScoping()
		{
		// set same variable in multiple contexts to different values
		MuseExecutionContext project_context = new ProjectExecutionContext(_project);
		TestSuiteExecutionContext suite_context = new DefaultTestSuiteExecutionContext(project_context, new SimpleTestSuite());
		final StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
		SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(suite_context, new SteppedTest(step));
		SingleStepExecutionContext step_context = new SingleStepExecutionContext(test_context, step, true);

		// ensure the correct values are retrieved from each level
		final String varname = "var1";
		project_context.setVariable(varname, "projectval");
		suite_context.setVariable(varname, "suiteval");
		test_context.setVariable(varname, "testval");
		step_context.setVariable(varname, "stepval");

		Assert.assertEquals("projectval", project_context.getVariable(varname));
		Assert.assertEquals("suiteval", suite_context.getVariable(varname));
		Assert.assertEquals("testval", test_context.getVariable(varname));
		Assert.assertEquals("stepval", step_context.getVariable(varname));

		Assert.assertEquals("projectval", step_context.getVariable(varname, VariableQueryScope.Project));
		Assert.assertEquals("suiteval", step_context.getVariable(varname, VariableQueryScope.Suite));
		Assert.assertEquals("testval", step_context.getVariable(varname, VariableQueryScope.Execution));
		Assert.assertEquals("stepval", step_context.getVariable(varname, VariableQueryScope.Local));
		}

	@Test
	public void createVariable()
		{
		String varname = _context.createVariable("varprefix-", "value");
		Assert.assertTrue(varname.startsWith("varprefix-"));
		Assert.assertEquals("value", _context.getVariable(varname));
		}

	@Test
	public void createVariableTwice()
		{
		String varname1 = _context.createVariable("varprefix-", "value1");
		String varname2 = _context.createVariable("varprefix-", "value2");
		Assert.assertNotEquals(varname1, varname2);
		Assert.assertEquals("value1", _context.getVariable(varname1));
		Assert.assertEquals("value2", _context.getVariable(varname2));
		}

	@Test
	public void shutShuttables()
		{
		// create a test with a step that creates a Shuttable resource
		MuseProject project = new SimpleProject();
		StepConfiguration step = new StepConfiguration(MockStepCreatesShuttable.TYPE_ID);
		SteppedTest test = new SteppedTest(step);

		// run the test
		BasicTestConfiguration test_config = new BasicTestConfiguration(test);
		final EventLogger logger = new EventLogger();
		TestResult result = TestRunHelper.runTest(project, test_config, logger);
		Assert.assertTrue(result.isPass());

		// verify the resource was created and closed
		Assert.assertTrue("The step did not run", logger.getLog().findEvents(event ->
		{
		final String description = EventTypes.DEFAULT.findType(event).getDescription(event);
		return description != null && description.contains(EXECUTE_MESSAGE);
		}).size() == 1);
		MockShuttable shuttable = (MockShuttable) test_config.context().getVariable(MockStepCreatesShuttable.SHUTTABLE_VAR_NAME);
		Assert.assertNotNull(shuttable);
		Assert.assertTrue(shuttable.isShutdown());
		}

	@Test
	public void queueNewEventsDuringProcessing()
		{
		final StepConfiguration step_config = new StepConfiguration(LogMessage.TYPE_ID);
		DefaultTestExecutionContext context = new DefaultTestExecutionContext(new SimpleProject(), new SteppedTest(step_config));

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

		Assert.assertEquals(2, events.size());
		Assert.assertTrue(event1 == events.get(0));
		Assert.assertTrue(event2.get() == events.get(1));
		}

	@Test
	public void getTestExecutionId()
		{
		MuseTest test = new MockTest("test1");
		TestExecutionContext context = new DefaultTestExecutionContext(_context, test);
		String id = context.getTestExecutionId();
		Assert.assertNotNull(id);
		}

	@Test
	public void getTestExecutionIdInSuite()
		{
		MuseTestSuite suite = new SimpleTestSuite();
		suite.setId("suite1");
		TestSuiteExecutionContext suite_context = new DefaultTestSuiteExecutionContext(_context, suite);
		MuseTest test = new MockTest("test1");
		TestExecutionContext context = new DefaultTestExecutionContext(suite_context, test);
		String id1 = context.getTestExecutionId();
		Assert.assertNotNull(id1);
		Assert.assertTrue(context.getTestExecutionId().length() >= test.getId().length());

		Assert.assertNotEquals(id1, new DefaultTestExecutionContext(_context, test).getTestExecutionId()); // should be unique for each test context
		}

	@Test
	public void successfulPluginInitEvent() throws MuseExecutionError
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
		Assert.assertEquals(0, fail_count);
		Assert.assertEquals(1, context._events_raised.size());
		final MuseEvent event = context._events_raised.get(0);
		Assert.assertEquals(PluginInitializedEventType.TYPE_ID, event.getTypeId());
		Assert.assertFalse(event.hasTag(MuseEvent.ERROR));
		Assert.assertFalse(event.hasTag(MuseEvent.FAILURE));
		Assert.assertTrue(event.getDescription().contains(success_plugin.getId()));

		context.cleanup();
		Assert.assertTrue(is_shutdown.get());
		}

	@Test
	public void failedPluginInitEvent() throws MuseExecutionError
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
		Assert.assertEquals(1, fail_count);
		Assert.assertEquals(1, context._events_raised.size());
		final MuseEvent event = context._events_raised.get(0);
		Assert.assertEquals(PluginInitializedEventType.TYPE_ID, event.getTypeId());
		Assert.assertTrue(event.hasTag(MuseEvent.ERROR));
		Assert.assertTrue(event.getDescription().contains(fail_plugin.getId()));
		Assert.assertTrue(new PluginInitializedEventType().getDescription(event).contains("failed"));

		context.cleanup();
		Assert.assertFalse(is_shutdown.get());  // failed plugins aren't shutdown()
		}

	@Test
	public void pluginShutdownOrder() throws MuseExecutionError
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

		Assert.assertEquals(0, context.initializePlugins());

		context.cleanup();
		Assert.assertEquals(2, shutdown_plugin_ids.size());
		Assert.assertEquals("plugin2", shutdown_plugin_ids.get(0));
		Assert.assertEquals("plugin1", shutdown_plugin_ids.get(1));
		}

	@Before
	public void setup()
		{
		_project = new SimpleProject();
		_context = new ProjectExecutionContext(_project);
		}

	private MuseExecutionContext _context;
	private MuseProject _project;
	}

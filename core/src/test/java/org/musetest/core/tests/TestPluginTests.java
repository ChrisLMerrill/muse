package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.test.plugins.TypeChangeEvent;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;
import org.musetest.core.variables.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestPluginTests
	{
	@Test
	public void testDefaultsInitializer() throws MuseExecutionError
		{
		MuseTest test = new SteppedTest();
		test.setDefaultVariable("var1", ValueSourceConfiguration.forValue("value1"));
		SimpleProject project = new SimpleProject();
		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		TestDefaultsInitializer initializer = new TestDefaultsInitializer(context);
		initializer.initialize(context);

		Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
		}

	@Test
	public void allProjectVariables() throws IOException
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();

		VariableList list = new VariableList();
		list.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
		project.getResourceStorage().addResource(list);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);

		// no variables will be injected, because there are no plugins
		Assert.assertNull("variable missing", context.getVariable("var1"));
		}

	@Test
	public void filteredProjectVariables() throws MuseExecutionError, IOException
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();

		VariableList list1 = new VariableList();
		list1.setId("list1");
		list1.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
		project.getResourceStorage().addResource(list1);

		VariableList list2 = new VariableList();
		list2.setId("list2");
		list2.addVariable("var2", ValueSourceConfiguration.forValue("value2"));
		project.getResourceStorage().addResource(list2);

		final TestPluginConfiguration config = new TestPluginConfiguration();
		config.setTypeId(VariableListInitializer.TYPE_ID);
		config.setApplyCondition(ValueSourceConfiguration.forValue(true));
		config.addParameter(VariableListInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list2"));

		final TestPluginsConfiguration initializers = new TestPluginsConfiguration();
		initializers.setApplyToTestCondition(ValueSourceConfiguration.forValue(true));
		initializers.addPlugin(config);
		project.getResourceStorage().addResource(initializers);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		context.initializePlugins(null);

		Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
		Assert.assertEquals("variable present, but should not be", null, context.getVariable("var1"));
		}

	@Test
	public void filteredByVarlistId() throws MuseExecutionError, IOException
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();

		VariableList list1 = new VariableList();
		list1.setId("list1");
		list1.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
		project.getResourceStorage().addResource(list1);

		VariableList list2 = new VariableList();
		list2.setId("list2");
		list2.addVariable("var2", ValueSourceConfiguration.forValue("value2"));
		project.getResourceStorage().addResource(list2);

		TestPluginsConfiguration configurations = new TestPluginsConfiguration();
		configurations.setApplyToTestCondition(ValueSourceConfiguration.forValue(true));

		TestPluginConfiguration include1 = new TestPluginConfiguration();
		include1.setApplyCondition(ValueSourceConfiguration.forValue(true));
		include1.setTypeId(VariableListInitializer.TYPE_ID);
		include1.addSource(VariableListInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list1"));
		include1.setApplyCondition(ValueSourceConfiguration.forValue(false));
		configurations.addPlugin(include1);

		TestPluginConfiguration include2 = new TestPluginConfiguration();
		include2.setApplyCondition(ValueSourceConfiguration.forValue(true));
		include2.setTypeId(VariableListInitializer.TYPE_ID);
		include2.addSource(VariableListInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list2"));
		include2.setApplyCondition(ValueSourceConfiguration.forValue(true));
		configurations.addPlugin(include2);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		TestPlugins.applyConditionally(configurations, context);
		context.initializePlugins(null);

		Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
		Assert.assertEquals("variable present, but should not be", null, context.getVariable("var1"));
		}

	@Test
	public void variableMapInitializer()
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();
		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		Map<String, Object> vars = new HashMap<>();
		vars.put("var1", "value1");
		VariableMapInitializer initializer = new VariableMapInitializer(vars);
		initializer.initialize(context);

		Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
		}

	@Test
	public void maintainInitializerOrder() throws IOException, MuseExecutionError
		{
		// add a bunch of references to lists that all init the same variable. Make sure that the LAST one sticks.
		MuseProject project = new SimpleProject();
		String last_value = null;
		final String var_name = "var1";

		TestPluginsConfiguration initializers = new TestPluginsConfiguration();
		initializers.setApplyToTestCondition(ValueSourceConfiguration.forValue(true));

		List<VariableList> list_of_lists = new ArrayList<>();
		for (int i = 0; i < 10; i++)
			{
			// create a list
			VariableList list = new VariableList();
			last_value = "value" + i;
			list.addVariable(var_name, ValueSourceConfiguration.forValue(last_value));
			String list_id = UUID.randomUUID().toString();
			list.setId(list_id);

			// add an initializer condition for this list
			TestPluginConfiguration init_config = new TestPluginConfiguration();
			init_config.setTypeId(VariableListInitializer.TYPE_ID);
			init_config.setApplyCondition(ValueSourceConfiguration.forValue(true));
			init_config.addParameter(VariableListInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue(list_id));
			initializers.addPlugin(init_config);

			// add to project
			list_of_lists.add(list);
			}
		project.getResourceStorage().addResource(initializers);

		// Add the lists in reverse order. We want to ensure they are initialized in the order that they appear
		// in the initializer list...regardless of the order they appear in the project.
		Collections.reverse(list_of_lists);
		for (VariableList list : list_of_lists)
			project.getResourceStorage().addResource(list);

		MuseExecutionContext context = new BaseExecutionContext(project);
		TestPlugins.applyConditionally(initializers, context);
		context.initializePlugins(null);

		Assert.assertEquals(last_value, context.getVariable(var_name));
		}

	@Test
	public void changeTypeEvent()
	    {
	    TestPluginConfiguration config = new TestPluginConfiguration();

	    final AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    config.addChangeListener(event_holder::set);

	    config.setTypeId("new_type");
	    Assert.assertNotNull("no event received", event_holder.get());
	    Assert.assertTrue("wrong type of event", event_holder.get() instanceof TypeChangeEvent);
	    Assert.assertTrue("wrong event target", config == ((TypeChangeEvent) event_holder.get()).getConfig()); // yes, this is a reference equality check
	    Assert.assertNull("old type should be null",  ((TypeChangeEvent) event_holder.get()).getOldType());
	    Assert.assertEquals("new type is wrong", "new_type", ((TypeChangeEvent) event_holder.get()).getNewType());
	    }

	@Test
	public void replaceApplyCondition()
	    {
	    TestPluginConfiguration config = new TestPluginConfiguration();

	    final AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    config.addChangeListener(event_holder::set);

	    config.setApplyCondition(ValueSourceConfiguration.forValue(true));
	    Assert.assertNotNull("no event received", event_holder.get());
	    Assert.assertTrue("wrong type of event", event_holder.get() instanceof ApplyConditionChangeEvent);
	    Assert.assertTrue("wrong event target", config == ((ApplyConditionChangeEvent) event_holder.get()).getConfig()); // yes, this is a reference equality check
	    Assert.assertNull("old condition should be null",  ((ApplyConditionChangeEvent) event_holder.get()).getOldCondition());
	    Assert.assertEquals("new condition is wrong", ValueSourceConfiguration.forValue(true), ((ApplyConditionChangeEvent) event_holder.get()).getNewCondition());
	    }

	@Test
	public void changeApplyCondition()
	    {
	    TestPluginConfiguration config = new TestPluginConfiguration();
	    config.setApplyCondition(ValueSourceConfiguration.forValue(true));

	    final AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    config.addChangeListener(event_holder::set);

	    config.getApplyCondition().addSource("abc", ValueSourceConfiguration.forValue(123));
	    Assert.assertNotNull("no event received", event_holder.get());
	    Assert.assertTrue("wrong type of event", event_holder.get() instanceof NamedSourceAddedEvent);
	    // don't need to check the specifics of the event...as that is delegated functionality covered by other tests. Only need to know this listener was wired into the event chain correctly.
	    }

	@Test
	public void addConfigEvent()
	    {
	    TestPluginsConfiguration configs = new TestPluginsConfiguration();
	    AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    configs.addChangeListener(event_holder::set);
	    
	    TestPluginConfiguration config = new TestPluginConfiguration();
	    config.setTypeId("config1");
	    configs.addPlugin(config);

	    Assert.assertNotNull(event_holder.get());
	    Assert.assertTrue(event_holder.get() instanceof TestPluginsConfiguration.ConfigAddedEvent);
	    TestPluginsConfiguration.ConfigAddedEvent event = (TestPluginsConfiguration.ConfigAddedEvent) event_holder.get();
	    Assert.assertEquals(config, event.getAddedConfig());
	    }

	@Test
	public void insertConfigEvent()
	    {
	    TestPluginsConfiguration configs = new TestPluginsConfiguration();
	    configs.addPlugin(new TestPluginConfiguration());
	    configs.addPlugin(new TestPluginConfiguration());
	    AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    configs.addChangeListener(event_holder::set);

	    TestPluginConfiguration added_config = new TestPluginConfiguration();
	    added_config.setTypeId("config1");
	    configs.addPlugin(added_config, 1);

	    Assert.assertNotNull(event_holder.get());
	    Assert.assertTrue(event_holder.get() instanceof TestPluginsConfiguration.ConfigAddedEvent);
	    TestPluginsConfiguration.ConfigAddedEvent event = (TestPluginsConfiguration.ConfigAddedEvent) event_holder.get();
	    Assert.assertTrue(added_config == event.getAddedConfig());
	    Assert.assertEquals(1, event.getIndex());
	    Assert.assertTrue(added_config == configs.getPlugins().get(1));
	    }

	@Test
	public void removeConfigEvent()
	    {
	    TestPluginsConfiguration configs = new TestPluginsConfiguration();
	    TestPluginConfiguration config = new TestPluginConfiguration();
	    config.setTypeId("config1");
	    configs.addPlugin(config);

	    AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    configs.addChangeListener(event_holder::set);
	    configs.removeConfiguration(config);

	    Assert.assertNotNull(event_holder.get());
	    Assert.assertTrue(event_holder.get() instanceof TestPluginsConfiguration.ConfigRemovedEvent);
	    TestPluginsConfiguration.ConfigRemovedEvent event = (TestPluginsConfiguration.ConfigRemovedEvent) event_holder.get();
	    Assert.assertEquals(config, event.getRemovedConfig());
	    }
	}
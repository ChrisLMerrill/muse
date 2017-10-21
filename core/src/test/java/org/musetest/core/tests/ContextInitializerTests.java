package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.context.initializers.TypeChangeEvent;
import org.musetest.core.project.*;
import org.musetest.core.steptest.*;
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
public class ContextInitializerTests
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
	public void allProjectVariables() throws MuseExecutionError, IOException
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();

		VariableList list = new VariableList();
		list.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
		project.getResourceStorage().addResource(list);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);

		// no variables will be injected, because there are no context initializers
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

		final ContextInitializerConfiguration config = new ContextInitializerConfiguration();
		config.setTypeId(VariableListContextInitializer.TYPE_ID);
		config.setApplyCondition(ValueSourceConfiguration.forValue(true));
		config.addParameter(VariableListContextInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list2"));

		final ContextInitializersConfiguration initializers = new ContextInitializersConfiguration();
		initializers.setApplyToTestCondition(ValueSourceConfiguration.forValue(true));
		initializers.addConfiguration(config);
		project.getResourceStorage().addResource(initializers);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		context.runInitializers();

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

		ContextInitializersConfiguration configurations = new ContextInitializersConfiguration();
		configurations.setApplyToTestCondition(ValueSourceConfiguration.forValue(true));

		ContextInitializerConfiguration include1 = new ContextInitializerConfiguration();
		include1.setApplyCondition(ValueSourceConfiguration.forValue(true));
		include1.setTypeId(VariableListContextInitializer.TYPE_ID);
		include1.addSource(VariableListContextInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list1"));
		include1.setApplyCondition(ValueSourceConfiguration.forValue(false));
		configurations.addConfiguration(include1);

		ContextInitializerConfiguration include2 = new ContextInitializerConfiguration();
		include2.setApplyCondition(ValueSourceConfiguration.forValue(true));
		include2.setTypeId(VariableListContextInitializer.TYPE_ID);
		include2.addSource(VariableListContextInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list2"));
		include2.setApplyCondition(ValueSourceConfiguration.forValue(true));
		configurations.addConfiguration(include2);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		ContextInitializers.applyConditionally(configurations, context);
		context.runInitializers();

		Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
		Assert.assertEquals("variable present, but should not be", null, context.getVariable("var1"));
		}

	@Test
	public void variableMapInitializer() throws MuseExecutionError
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

		ContextInitializersConfiguration initializers = new ContextInitializersConfiguration();
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
			ContextInitializerConfiguration init_config = new ContextInitializerConfiguration();
			init_config.setTypeId(VariableListContextInitializer.TYPE_ID);
			init_config.setApplyCondition(ValueSourceConfiguration.forValue(true));
			init_config.addParameter(VariableListContextInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue(list_id));
			initializers.addConfiguration(init_config);

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
		ContextInitializers.applyConditionally(initializers, context);
		context.runInitializers();

		Assert.assertEquals(last_value, context.getVariable(var_name));
		}

	@Test
	public void changeTypeEvent()
	    {
	    ContextInitializerConfiguration config = new ContextInitializerConfiguration();

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
	    ContextInitializerConfiguration config = new ContextInitializerConfiguration();

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
	    ContextInitializerConfiguration config = new ContextInitializerConfiguration();
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
	    ContextInitializersConfiguration configs = new ContextInitializersConfiguration();
	    AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    configs.addChangeListener(event_holder::set);
	    
	    ContextInitializerConfiguration config = new ContextInitializerConfiguration();
	    config.setTypeId("config1");
	    configs.addConfiguration(config);

	    Assert.assertNotNull(event_holder.get());
	    Assert.assertTrue(event_holder.get() instanceof ContextInitializersConfiguration.ConfigAddedEvent);
	    ContextInitializersConfiguration.ConfigAddedEvent event = (ContextInitializersConfiguration.ConfigAddedEvent) event_holder.get();
	    Assert.assertEquals(config, event.getAddedConfig());
	    }

	@Test
	public void insertConfigEvent()
	    {
	    ContextInitializersConfiguration configs = new ContextInitializersConfiguration();
	    configs.addConfiguration(new ContextInitializerConfiguration());
	    configs.addConfiguration(new ContextInitializerConfiguration());
	    AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    configs.addChangeListener(event_holder::set);

	    ContextInitializerConfiguration added_config = new ContextInitializerConfiguration();
	    added_config.setTypeId("config1");
	    configs.addConfiguration(added_config, 1);

	    Assert.assertNotNull(event_holder.get());
	    Assert.assertTrue(event_holder.get() instanceof ContextInitializersConfiguration.ConfigAddedEvent);
	    ContextInitializersConfiguration.ConfigAddedEvent event = (ContextInitializersConfiguration.ConfigAddedEvent) event_holder.get();
	    Assert.assertTrue(added_config == event.getAddedConfig());
	    Assert.assertEquals(1, event.getIndex());
	    Assert.assertTrue(added_config == configs.getInitializers().get(1));
	    }

	@Test
	public void removeConfigEvent()
	    {
	    ContextInitializersConfiguration configs = new ContextInitializersConfiguration();
	    ContextInitializerConfiguration config = new ContextInitializerConfiguration();
	    config.setTypeId("config1");
	    configs.addConfiguration(config);

	    AtomicReference<ChangeEvent> event_holder = new AtomicReference<>(null);
	    configs.addChangeListener(event_holder::set);
	    configs.removeConfiguration(config);

	    Assert.assertNotNull(event_holder.get());
	    Assert.assertTrue(event_holder.get() instanceof ContextInitializersConfiguration.ConfigRemovedEvent);
	    ContextInitializersConfiguration.ConfigRemovedEvent event = (ContextInitializersConfiguration.ConfigRemovedEvent) event_holder.get();
	    Assert.assertEquals(config, event.getRemovedConfig());
	    }
	}
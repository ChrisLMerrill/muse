package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.project.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.io.*;
import java.util.*;

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
		config.setInitializerType(VariableListContextInitializer.TYPE_ID);
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
		include1.setInitializerType(VariableListContextInitializer.TYPE_ID);
		include1.addSource(VariableListContextInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list1"));
		include1.setApplyCondition(ValueSourceConfiguration.forValue(false));
		configurations.addConfiguration(include1);

		ContextInitializerConfiguration include2 = new ContextInitializerConfiguration();
		include2.setApplyCondition(ValueSourceConfiguration.forValue(true));
		include2.setInitializerType(VariableListContextInitializer.TYPE_ID);
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
	public void listeners()
		{
		// add/remove ContextInitializerConfiguration from a ContextInitializersConfiguration
		// and listen for the change events

		Assert.assertTrue("test not finished", false);
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
			init_config.setInitializerType(VariableListContextInitializer.TYPE_ID);
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

	}
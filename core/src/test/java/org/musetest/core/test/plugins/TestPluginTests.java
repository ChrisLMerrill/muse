package org.musetest.core.test.plugins;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.plugins.*;
import org.musetest.core.project.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TestPluginTests
	{
	@Test
    void testDefaultsInitializer() throws MuseExecutionError
		{
		MuseTest test = new SteppedTest();
		test.setDefaultVariable("var1", ValueSourceConfiguration.forValue("value1"));
		SimpleProject project = new SimpleProject();
		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		TestDefaultsInitializer initializer = new TestDefaultsInitializerConfiguration().createPlugin();
		initializer.initialize(context);

		Assertions.assertEquals("value1", context.getVariable("var1"), "variable missing");
		}

	@Test
    void allProjectVariables() throws IOException
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();

		VariableList list = new VariableList();
		list.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
		project.getResourceStorage().addResource(list);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);

		// no variables will be injected, because there are no plugins
		Assertions.assertNull(context.getVariable("var1"), "variable missing");
		}

	@Test
    void filteredProjectVariables() throws MuseExecutionError, IOException
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

		VariableListInitializerConfiguration include2 = new VariableListInitializerConfiguration();
		include2.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
		include2.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
		include2.parameters().addSource(VariableListInitializerConfiguration.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list2"));
		project.getResourceStorage().addResource(include2);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		Plugins.setup(context);
		context.initializePlugins();

		Assertions.assertEquals("value2", context.getVariable("var2"), "variable missing");
        Assertions.assertNull(context.getVariable("var1"), "variable present, but should not be");
		}

	@Test
    void filteredByVarlistId() throws MuseExecutionError, IOException
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

		VariableListInitializerConfiguration include1 = new VariableListInitializerConfiguration();
		include1.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
		include1.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(false));
		include1.parameters().addSource(VariableListInitializerConfiguration.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list1"));
		project.getResourceStorage().addResource(include1);

		VariableListInitializerConfiguration include2 = new VariableListInitializerConfiguration();
		include2.parameters().addSource(GenericConfigurablePlugin.AUTO_APPLY_PARAM, ValueSourceConfiguration.forValue(true));
		include2.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
		include2.parameters().addSource(VariableListInitializerConfiguration.LIST_ID_PARAM, ValueSourceConfiguration.forValue("list2"));
		project.getResourceStorage().addResource(include2);

		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		Plugins.setup(context);
		context.initializePlugins();

		Assertions.assertEquals("value2", context.getVariable("var2"), "variable missing");
        Assertions.assertNull(context.getVariable("var1"), "variable present, but should not be");
		}

	@Test
    void variableMapInitializer()
		{
		MuseTest test = new SteppedTest();
		SimpleProject project = new SimpleProject();
		TestExecutionContext context = new DefaultTestExecutionContext(project, test);
		Map<String, Object> vars = new HashMap<>();
		vars.put("var1", "value1");
		VariableMapInitializer initializer = new VariableMapInitializer(vars);
		initializer.initialize(context);

		Assertions.assertEquals("value1", context.getVariable("var1"), "variable missing");
		}
	}
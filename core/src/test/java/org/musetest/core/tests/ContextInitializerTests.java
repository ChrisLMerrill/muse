package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.sysvar.*;
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

        VariableList list2 = new VariableList();
        list2.addVariable("var2", ValueSourceConfiguration.forValue("value2"));
        project.getResourceStorage().addResource(list2);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        VariableListsInitializer initializer = new VariableListsInitializer();
        initializer.initialize(context);

        Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
        Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
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

        ContextInitializerConfigurations configurations = new ContextInitializerConfigurations();
        VariableListContextInitializerConfiguration config = new VariableListContextInitializerConfiguration();
        config.setVariableListId("list2");
        config.setIncludeCondition(ValueSourceConfiguration.forValue(Boolean.TRUE));
        configurations.addVariableListInitializer(config);
        project.getResourceStorage().addResource(configurations);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        VariableListsInitializer initializer = new VariableListsInitializer();
        initializer.initialize(context);

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

        ContextInitializerConfigurations configurations = new ContextInitializerConfigurations();
        VariableListContextInitializerConfiguration config = new VariableListContextInitializerConfiguration();
        config.setVariableListId("list2");
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forValue("list2"));
        condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forTypeWithSource(SystemVariableSource.TYPE_ID, ValueSourceConfiguration.forValue(ProjectVarsInitializerSysvarProvider.SYSVAR_NAME)));
        config.setIncludeCondition(condition);
        configurations.addVariableListInitializer(config);
        project.getResourceStorage().addResource(configurations);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        VariableListsInitializer initializer = new VariableListsInitializer();
        initializer.initialize(context);

        Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
        Assert.assertEquals("variable present, but should not be", null, context.getVariable("var1"));
        }

    @Test
    public void variablesInitializer() throws MuseExecutionError
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
        TestListener listener = new TestListener();
        ContextInitializerConfigurations main_config = new ContextInitializerConfigurations();
        main_config.addContextInitializerChangeListener(listener);

        // add a new config
        VariableListContextInitializerConfiguration config = new VariableListContextInitializerConfiguration();
        main_config.addVariableListInitializer(config);
        Assert.assertEquals(config, listener._added);
        Assert.assertNull(listener._removed);

        // remove the config
        listener._added = null;
        main_config.removeVariableListInitializer(config);
        Assert.assertEquals(config, listener._removed);
        Assert.assertNull(listener._added);

        // make sure the listener removal works
        main_config.removeContextInitializerChangeListener(listener);
        listener._removed = null;
        main_config.addVariableListInitializer(config);
        Assert.assertNull(listener._added);
        }

    @Test
    public void maintainInitializerOrder() throws IOException, MuseExecutionError
        {
        // add a bunch of references to lists that all init the same variable. Make sure that the LAST one sticks.
        MuseProject project = new SimpleProject();
        String last_value = null;
        final String var_name = "var1";

        ContextInitializerConfigurations initializers = new ContextInitializerConfigurations();
        project.getResourceStorage().addResource(initializers);

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
            VariableListContextInitializerConfiguration initializer = new VariableListContextInitializerConfiguration();
            initializer.setVariableListId(list_id);
            initializer.setIncludeCondition(ValueSourceConfiguration.forValue(true));
            initializers.addVariableListInitializer(initializer);

            // add to project
            list_of_lists.add(list);
            }

        // Add them in reverse order. We want to ensure they are initialized in the order that they appear
        // in the initializer list...regardless of the order they appear in the project.
        Collections.reverse(list_of_lists);
        for (VariableList list : list_of_lists)
            project.getResourceStorage().addResource(list);

        MuseExecutionContext context = new BaseExecutionContext(project);
        new VariableListsInitializer().initialize(context);

        Assert.assertEquals(last_value, context.getVariable(var_name));
        }

    private class TestListener extends ContextInitializerChangeListener
        {
        @Override
        public void variableListInitializerAdded(VariableListContextInitializerConfiguration config)
            {
            _added = config;
            }

        @Override
        public void variableListInitializerRemoved(VariableListContextInitializerConfiguration config)
            {
            _removed = config;
            }

        VariableListContextInitializerConfiguration _added = null;
        VariableListContextInitializerConfiguration _removed = null;
        }
    }



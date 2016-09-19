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
        initializer.initialize(project, context);

        Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
        }

    @Test
    public void allProjectVariables() throws MuseExecutionError
        {
        MuseTest test = new SteppedTest();
        SimpleProject project = new SimpleProject();

        VariableList list = new VariableList();
        list.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
        project.addResource(list);

        VariableList list2 = new VariableList();
        list2.addVariable("var2", ValueSourceConfiguration.forValue("value2"));
        project.addResource(list2);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        VariableListsInitializer initializer = new VariableListsInitializer();
        initializer.initialize(project, context);

        Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
        Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
        }

    @Test
    public void filteredProjectVariables() throws MuseExecutionError
        {
        MuseTest test = new SteppedTest();
        SimpleProject project = new SimpleProject();

        VariableList list1 = new VariableList();
        list1.getMetadata().setId("list1");
        list1.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
        project.addResource(list1);

        VariableList list2 = new VariableList();
        list2.getMetadata().setId("list2");
        list2.addVariable("var2", ValueSourceConfiguration.forValue("value2"));
        project.addResource(list2);

        ContextInitializerConfigurations configurations = new ContextInitializerConfigurations();
        VariableListContextInitializerConfiguration config = new VariableListContextInitializerConfiguration();
        config.setVariableListId("list2");
        config.setIncludeCondition(ValueSourceConfiguration.forValue(Boolean.TRUE));
        configurations.addVariableListInitializer(config);
        project.addResource(configurations);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        VariableListsInitializer initializer = new VariableListsInitializer();
        initializer.initialize(project, context);

        Assert.assertEquals("variable missing", "value2", context.getVariable("var2"));
        Assert.assertEquals("variable present, but should not be", null, context.getVariable("var1"));
        }

    @Test
    public void filteredByVarlistId() throws MuseExecutionError
        {
        MuseTest test = new SteppedTest();
        SimpleProject project = new SimpleProject();

        VariableList list1 = new VariableList();
        list1.getMetadata().setId("list1");
        list1.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
        project.addResource(list1);

        VariableList list2 = new VariableList();
        list2.getMetadata().setId("list2");
        list2.addVariable("var2", ValueSourceConfiguration.forValue("value2"));
        project.addResource(list2);

        ContextInitializerConfigurations configurations = new ContextInitializerConfigurations();
        VariableListContextInitializerConfiguration config = new VariableListContextInitializerConfiguration();
        config.setVariableListId("list2");
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, ValueSourceConfiguration.forValue("list2"));
        condition.addSource(EqualityCondition.RIGHT_PARAM, ValueSourceConfiguration.forTypeWithSource(SystemVariableSource.TYPE_ID, ValueSourceConfiguration.forValue(ProjectVarsInitializerSysvarProvider.SYSVAR_NAME)));
        config.setIncludeCondition(condition);
        configurations.addVariableListInitializer(config);
        project.addResource(configurations);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        VariableListsInitializer initializer = new VariableListsInitializer();
        initializer.initialize(project, context);

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
        initializer.initialize(project, context);

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



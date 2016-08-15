package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.project.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author ©2015 Web Performance, Inc
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
    public void allProjectVariablesInitializer() throws MuseExecutionError
        {
        MuseTest test = new SteppedTest();
        VariableList list = new VariableList();
        list.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
        SimpleProject project = new SimpleProject();
        project.addResource(list);
        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        ProjectVariablesInitializer initializer = new ProjectVariablesInitializer();
        initializer.initialize(project, context);

        Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
        }

    @Test
    public void filteredProjectVariablesInitializer() throws MuseExecutionError
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
        configurations.addVariableListCondition(config);
        project.addResource(configurations);

        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        ProjectVariablesInitializer initializer = new ProjectVariablesInitializer();
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
        VariablesInitializer initializer = new VariablesInitializer(vars);
        initializer.initialize(project, context);

        Assert.assertEquals("variable missing", "value1", context.getVariable("var1"));
        }
    }



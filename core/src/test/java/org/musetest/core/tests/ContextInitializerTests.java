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

        Assert.assertEquals("variable not set", "value1", context.getVariable("var1"));
        }

    @Test
    public void projectVariablesInitializer() throws MuseExecutionError
        {
        MuseTest test = new SteppedTest();
        VariableList list = new VariableList();
        list.addVariable("var1", ValueSourceConfiguration.forValue("value1"));
        SimpleProject project = new SimpleProject();
        project.addResource(list);
        TestExecutionContext context = new DefaultTestExecutionContext(project, test);
        ProjectVariablesInitializer initializer = new ProjectVariablesInitializer();
        initializer.initialize(project, context);

        Assert.assertEquals("variable not set", "value1", context.getVariable("var1"));
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

        Assert.assertEquals("variable not set", "value1", context.getVariable("var1"));
        }
    }



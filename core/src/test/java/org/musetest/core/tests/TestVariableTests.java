package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.SteppedTest;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.io.*;
import java.util.*;

/**
 * These tests ensure the correct ContextInitializers are run by the test engine. Correctness of
 * the initializers should be tested in ContextInitializerTests.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestVariableTests
    {
    @Test
    public void variableSetFromDefaults()
        {
        Map<String, ValueSourceConfiguration> default_vars = new HashMap<>();
        default_vars.put(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        SteppedTest test = getTest();
        test.setDefaultVariables(default_vars);

        MuseTestResult result = test.execute(new DefaultTestExecutionContext(new SimpleProject(), test));
        Assert.assertTrue(result.isPass());
        }

    @Test
    public void variableSetFromProjectVariableList() throws IOException, MuseExecutionError
        {
        SteppedTest test = getTest();
        MuseProject project = new SimpleProject();

        VariableList list = new VariableList();
        final String list_id = "list123";
        list.setId(list_id);
        project.getResourceStorage().addResource(list);
        list.addVariable(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        ContextInitializerConfiguration initializer = new ContextInitializerConfiguration();
        initializer.setInitializerType(VariableListContextInitializer.TYPE_ID);
        initializer.setApplyCondition(ValueSourceConfiguration.forValue(true));
        initializer.addParameter(VariableListContextInitializer.LIST_ID_PARAM, ValueSourceConfiguration.forValue(list_id));
        ContextInitializersConfiguration initializers = new ContextInitializersConfiguration();
        initializers.setApplyToTestCondition(ValueSourceConfiguration.forValue(true));
        initializers.addConfiguration(initializer);

        DefaultTestExecutionContext context = new DefaultTestExecutionContext(project, test);
        ContextInitializers.applyConditionally(initializers, context);
        MuseTestResult result = test.execute(context);
        Assert.assertTrue(result.isPass());
        }

    private SteppedTest getTest()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        ValueSourceConfiguration equals = BinaryCondition.forSources(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(VAR_NAME)), ValueSourceConfiguration.forValue(VAR_VALUE));
        step.addSource(Verify.CONDITION_PARAM, equals);

        SteppedTest test = new SteppedTest(step);
        test.setId("id123");
        return test;
        }

    private final static String VAR_NAME = "var1";
    private final static long VAR_VALUE = 3L;
    }



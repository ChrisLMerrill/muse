package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.condition.*;
import org.museautomation.builtins.step.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.project.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.test.plugins.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.values.*;
import org.museautomation.core.variables.*;

import java.io.*;
import java.util.*;

/**
 * These tests ensure the correct TestPlugins are run by the test engine. Correctness of
 * the plugins should be tested in TestPluginTests.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TestVariableTests
    {
    @Test
    void variableSetFromDefaults()
	    {
        Map<String, ValueSourceConfiguration> default_vars = new HashMap<>();
        default_vars.put(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        SteppedTest test = getTest();
        test.setDefaultVariables(default_vars);

        Assertions.assertTrue(TestRunHelper.runTest(new SimpleProject(), test, new TestDefaultsInitializerConfiguration().createPlugin()).isPass());
        }

    @Test
    void variableSetFromProjectVariableList() throws IOException
        {
        SteppedTest test = getTest();
        MuseProject project = new SimpleProject();

        VariableList list = new VariableList();
        final String list_id = "list123";
        list.setId(list_id);
        project.getResourceStorage().addResource(list);
        list.addVariable(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        VariableListInitializerConfiguration config = new VariableListInitializerConfiguration();
        config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
        config.parameters().addSource(VariableListInitializerConfiguration.LIST_ID_PARAM, ValueSourceConfiguration.forValue(list_id));

        Assertions.assertTrue(TestRunHelper.runTest(project, test, config.createPlugin()).isPass());
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

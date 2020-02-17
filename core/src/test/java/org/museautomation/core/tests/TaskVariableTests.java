package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.condition.*;
import org.museautomation.builtins.step.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.project.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.task.plugins.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.values.*;
import org.museautomation.core.variables.*;

import java.io.*;
import java.util.*;

/**
 * These tests ensure the correct TaskPlugins are run by the task engine. Correctness of
 * the plugins should be tested in TaskPluginTests.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskVariableTests
    {
    @Test
    void variableSetFromDefaults()
	    {
        Map<String, ValueSourceConfiguration> default_vars = new HashMap<>();
        default_vars.put(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        SteppedTask task = getTask();
        task.setDefaultVariables(default_vars);

        Assertions.assertTrue(TaskRunHelper.runTask(new SimpleProject(), task, new TaskDefaultsInitializerConfiguration().createPlugin()).isPass());
        }

    @Test
    void variableSetFromProjectVariableList() throws IOException
        {
        SteppedTask task = getTask();
        MuseProject project = new SimpleProject();

        VariableList list = new VariableList();
        final String list_id = "list123";
        list.setId(list_id);
        project.getResourceStorage().addResource(list);
        list.addVariable(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        VariableListInitializerConfiguration config = new VariableListInitializerConfiguration();
        config.parameters().addSource(GenericConfigurablePlugin.APPLY_CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
        config.parameters().addSource(VariableListInitializerConfiguration.LIST_ID_PARAM, ValueSourceConfiguration.forValue(list_id));

        Assertions.assertTrue(TaskRunHelper.runTask(project, task, config.createPlugin()).isPass());
        }

    private SteppedTask getTask()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        ValueSourceConfiguration equals = BinaryCondition.forSources(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(VAR_NAME)), ValueSourceConfiguration.forValue(VAR_VALUE));
        step.addSource(Verify.CONDITION_PARAM, equals);

        SteppedTask task = new SteppedTask(step);
        task.setId("id123");
        return task;
        }

    private final static String VAR_NAME = "var1";
    private final static long VAR_VALUE = 3L;
    }

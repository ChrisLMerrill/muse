package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestVariableTests
    {
    @Test
    public void testVariableNotSet()
        {
        MuseTestResult result = getTest().execute(new DefaultTestExecutionContext());
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        }

    @Test
    public void testVariableSetManually()
        {
        TestExecutionContext context = new DefaultTestExecutionContext();
        context.setVariable(VAR_NAME, VAR_VALUE, VariableScope.Execution);

        MuseTestResult result = getTest().execute(context);
        Assert.assertTrue(result.isPass());
        }

    @Test
    public void testVariableSetAutomatically()
        {
        Map<String, ValueSourceConfiguration> default_vars = new HashMap<>();
        default_vars.put(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        SteppedTest test = getTest();
        test.setDefaultVariables(default_vars);

        MuseTestResult result = test.execute(new DefaultTestExecutionContext());
        Assert.assertTrue(result.isPass());
        }

    @Test
    public void projectDefaultVaraibles()
        {
        SteppedTest test = getTest();
        MuseProject project = new SimpleProject();

        VariableList list = new VariableList();
        project.addResource(list);
        list.addVariable(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        DefaultTestExecutionContext context = new DefaultTestExecutionContext(project);
        MuseTestResult result = test.execute(context);
        Assert.assertTrue(result.isPass());
        }

    private SteppedTest getTest()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        ValueSourceConfiguration equals = BinaryCondition.forSources(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(VAR_NAME)), ValueSourceConfiguration.forValue(VAR_VALUE));
        step.setSource(Verify.CONDITION_PARAM, equals);

        return new SteppedTest(step);
        }

    private final static String VAR_NAME = "var1";
    private final static long VAR_VALUE = 3L;
    }



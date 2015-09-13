package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestVariableTests
    {
    @Test
    public void testTestVariableNotSet()
        {
        MuseTestResult result = getTest().execute(new DefaultTestExecutionContext());
        Assert.assertEquals(MuseTestResultStatus.Failure, result.getStatus());
        }

    @Test
    public void testTestVariableSetManually()
        {
        TestExecutionContext context = new DefaultTestExecutionContext();
        context.setVariable(VAR_NAME, VAR_VALUE);

        MuseTestResult result = getTest().execute(context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        }

    @Test
    public void testTestVariableSetAutomatically()
        {
        Map<String, ValueSourceConfiguration> default_vars = new HashMap<>();
        default_vars.put(VAR_NAME, ValueSourceConfiguration.forValue(VAR_VALUE));

        SteppedTest test = getTest();
        test.setDefaultVariables(default_vars);

        MuseTestResult result = test.execute(new DefaultTestExecutionContext());
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        }

    private SteppedTest getTest()
        {
        StepConfiguration step = new StepConfiguration(Verify.TYPE_ID);
        ValueSourceConfiguration equals = BinaryCondition.forSources(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, ValueSourceConfiguration.forValue(VAR_NAME)), ValueSourceConfiguration.forValue(VAR_VALUE));
        step.addSource(Verify.CONDITION_PARAM, equals);

        return new SteppedTest(step);
        }

    private final static String VAR_NAME = "var1";
    private final static long VAR_VALUE = 3L;
    }



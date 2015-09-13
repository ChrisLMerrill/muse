package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.steptest.SteppedTestExecutor;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepConfigExecutionTests
    {
    @Test
    public void singleStep()
        {
        StepConfiguration step_a = new StepConfiguration(StoreVariable.TYPE_ID);
        step_a.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("X"));
        step_a.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(123L));

        SteppedTest test = new SteppedTest(step_a);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();

        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(123L, test_context.getVariable("X"));
        }

    @Test
    public void singleNestedStep()
        {
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        StepConfiguration child = new StepConfiguration();
        child.setType("store-variable");
        child.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("X"));
        child.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(123L));
        parent.addChild(child);

        SteppedTest test = new SteppedTest(parent);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();

        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(123L, test_context.getVariable("X"));
        }

    @Test
    public void twoNestedSteps()
        {
        StepConfiguration parent = new StepConfiguration();
        parent.setType("compound");

        StepConfiguration child1 = new StepConfiguration();
        child1.setType("store-variable");
        child1.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("X"));
        child1.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(123L));
        parent.addChild(child1);

        StepConfiguration child2 = new StepConfiguration();
        child2.setType("store-variable");
        child2.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("Y"));
        child2.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(456L));
        parent.addChild(child2);

        SteppedTest test = new SteppedTest(parent);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();

        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(123L, test_context.getVariable("X"));
        Assert.assertEquals(456L, test_context.getVariable("Y"));
        }

    // TODO test doubly-nested compound steps

    // TODO test step failure - e.g. verify(false)

    @Test
    public void stepMissingParameter()
        {
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        SteppedTest test = new SteppedTest(step_a);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();
        Assert.assertEquals(MuseTestResultStatus.Error, result.getStatus());
        }

    @Test
    public void stepParameterResolvesToNull()
        {
        StepConfiguration step_a = new StepConfiguration();
        step_a.setType("blahblah");
        step_a.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue(null));

        SteppedTest test = new SteppedTest(step_a);
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        SteppedTestExecutor executor = new SteppedTestExecutor(test, new DefaultSteppedTestExecutionContext(test_context));

        MuseTestResult result = executor.executeAll();
        Assert.assertEquals(MuseTestResultStatus.Error, result.getStatus());
        }


    }



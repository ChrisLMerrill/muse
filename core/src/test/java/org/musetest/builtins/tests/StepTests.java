package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepTests
    {
    /**
     * Ok...how do you test that something has been sent to the diagnostic log?  So this test doesn't actually test
     * the step. To do so, I think we would need to replace the logging implementation so that we could extract
     * the logged messages (perhaps with SLF4J Test: http://projects.lidalia.org.uk/slf4j-test/).  But then, how would
     * we see the rest of the logging for the unit tests?  Anyway, a human needs to inspect the log from this test to
     * see if this worked. My apologies :(
     */
    @Test
    public void testLogMessage() throws StepExecutionError
        {
        // this should log a message indicating there was no message parameter
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        try
            {
            MuseStep step = config.createStep();
            Assert.assertTrue("createStep() should have thrown an error due to a missing parameter", false);

            step.execute(new DummyStepExecutionContext() {});
            }
        catch (MuseInstantiationException e)
            {
            // ok!
            }

        // this should log a message indicating that the value source returned null
        try
            {
            config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(null));
            config.createStep();
            Assert.assertTrue("execute() should have thrown an error due to a null value", false);
            }
        catch (MuseInstantiationException e)
            {
            // ok!
            }

        // this should log a message
        config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("this is the message"));
        config.createStep().execute(new DummyStepExecutionContext() { });
        }

    @Test
    public void testStoreStringVariable() throws StepExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreVariable.TYPE_ID);
        config.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        config.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue("abc"));

        StepExecutionContext context = new DummyStepExecutionContext();
        MuseStep step = config.createStep();
        step.execute(context);
        Assert.assertEquals("abc", context.getTestExecutionContext().getVariable("var1"));
        }

    @Test
    public void testStoreIntegerVariable() throws StepExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreVariable.TYPE_ID);
        config.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var_int"));
        config.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(191L));

        MuseStep step = config.createStep();
        StepExecutionContext context = new DummyStepExecutionContext();
        step.execute(context);
        Assert.assertEquals(191L, context.getTestExecutionContext().getVariable("var_int"));
        }

    @Test
    public void testIncrementVariableBy1() throws StepExecutionError
        {
        StepConfiguration config = new StepConfiguration(IncrementVariable.TYPE_ID);
        config.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));

        MuseStep step = config.createStep();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.getTestExecutionContext().setVariable("var1", 3L);
        step.execute(context);
        Assert.assertEquals(4L, context.getTestExecutionContext().getVariable("var1"));
        }

    @Test
    public void testIncrementVariableByN() throws StepExecutionError
        {
        Long start_value = 101L;
        Long amount = 7L;

        StepConfiguration config = new StepConfiguration(IncrementVariable.TYPE_ID);
        config.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        config.addSource(IncrementVariable.AMOUNT_PARAM, ValueSourceConfiguration.forValue(amount));

        MuseStep step = config.createStep();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.getTestExecutionContext().setVariable("var1", start_value);
        step.execute(context);
        Assert.assertEquals(start_value + amount, context.getTestExecutionContext().getVariable("var1"));
        }

    @Test
    public void testVerifySuccess() throws StepExecutionError
        {
        EventLog log = new EventLog();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        test_context.addEventListener(log);

        ValueSourceConfiguration left = ValueSourceConfiguration.forValue("abc");
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue("abc");
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, left);
        condition.addSource(EqualityCondition.RIGHT_PARAM, right);
        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        config.addSource(Verify.CONDITION_PARAM, condition);
        MuseStep step = config.createStep();

        StepExecutionContext context = new DummyStepExecutionContext();
        StepExecutionResult result = step.execute(context);
        Assert.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        }

    @Test
    public void testVerifyFailed() throws StepExecutionError
        {
        EventLog log = new EventLog();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext();
        test_context.addEventListener(log);

        ValueSourceConfiguration left = ValueSourceConfiguration.forValue("abc");
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue("def");
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, left);
        condition.addSource(EqualityCondition.RIGHT_PARAM, right);
        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        config.addSource(Verify.CONDITION_PARAM, condition);
        MuseStep step = config.createStep();

        StepExecutionResult result = step.execute(new SimpleStepExecutionContext(new DefaultSteppedTestExecutionContext(test_context)));
        Assert.assertEquals(StepExecutionStatus.FAILURE, result.getStatus());
        }

    @Test
    public void testCallStep()
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());

        // create a macro to call
        String macro_id = "TestMacro";
        Macro macro = new Macro();
        StepConfiguration main = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        StepConfiguration store_step = new StepConfiguration(StoreVariable.TYPE_ID);
        store_step.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("abc"));
        store_step.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(123L));
        main.addChild(store_step);
        macro.getMetadata().setId(macro_id);
        macro.getMetadata().setType(ResourceTypes.Macro);
        macro.setStep(main);
        project.addResource(macro);

        // create a step and test that calls the macro above
        StepConfiguration call_macro = new StepConfiguration(CallMacroStep.TYPE_ID);
        call_macro.addSource(CallMacroStep.ID_PARAM, ValueSourceConfiguration.forValue(macro_id));
        SteppedTest test = new SteppedTest(call_macro);

        // verify that the macro runs when the test is executed
        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(123L, runner.getTestContext().getVariable("abc"));
        }
    }



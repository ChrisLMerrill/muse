package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.execution.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

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
    public void logMessage() throws MuseExecutionError
        {
        // this should log a message indicating there was no message parameter
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        try
            {
            MuseStep step = config.createStep();
            Assert.assertTrue("createStep() should have thrown an error due to a missing parameter", false);

            step.execute(new DummyStepExecutionContext());
            }
        catch (MuseInstantiationException e)
            {
            // ok!
            }

        // this should log a message
        config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("this is the message"));
        config.createStep().execute(new DummyStepExecutionContext());
        }

    @Test
    public void storeStringVariable() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreVariable.TYPE_ID);
        config.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        config.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue("abc"));

        StepExecutionContext context = new DummyStepExecutionContext();
        MuseStep step = config.createStep();
        step.execute(context);
        Assert.assertEquals("abc", context.getVariable("var1"));
        }

    @Test
    public void storeIntegerVariable() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreVariable.TYPE_ID);
        config.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var_int"));
        config.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(191L));

        MuseStep step = config.createStep();
        StepExecutionContext context = new DummyStepExecutionContext();
        step.execute(context);
        Assert.assertEquals(191L, context.getVariable("var_int"));
        }

    @Test
    public void incrementVariableBy1() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(IncrementVariable.TYPE_ID);
        config.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));

        MuseStep step = config.createStep();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.setVariable("var1", 3L);
        step.execute(context);
        Assert.assertEquals(4L, context.getVariable("var1"));
        }

    @Test
    public void incrementVariableByN() throws MuseExecutionError
        {
        Long start_value = 101L;
        Long amount = 7L;

        StepConfiguration config = new StepConfiguration(IncrementVariable.TYPE_ID);
        config.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        config.addSource(IncrementVariable.AMOUNT_PARAM, ValueSourceConfiguration.forValue(amount));

        MuseStep step = config.createStep();
        StepExecutionContext context = new DummyStepExecutionContext();
        context.setVariable("var1", start_value);
        step.execute(context);
        Assert.assertEquals(start_value + amount, context.getVariable("var1"));
        }

    @Test
    public void verifySuccess() throws MuseExecutionError
        {
        EventLog log = new EventLog();
        DefaultTestExecutionContext test_context = new DefaultTestExecutionContext(new SimpleProject(), null);
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
    public void verifyFailed() throws MuseExecutionError
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue("abc");
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue("def");
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(EqualityCondition.LEFT_PARAM, left);
        condition.addSource(EqualityCondition.RIGHT_PARAM, right);
        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        config.addSource(Verify.CONDITION_PARAM, condition);
        MuseStep step = config.createStep();

        StepExecutionResult result = step.execute(new DummyStepExecutionContext());
        Assert.assertEquals(StepExecutionStatus.FAILURE, result.getStatus());
        }

    @Test
    public void callMacro()
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());

        // create a macro to call
        String macro_id = "TestMacro";
        Macro macro = new Macro();
        StepConfiguration main = new StepConfiguration(BasicCompoundStep.TYPE_ID);

        final String message = "the message";
        StepConfiguration message_step = new StepConfiguration(LogMessage.TYPE_ID);
        message_step.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(message));
        main.addChild(message_step);
        macro.setId(macro_id);
        macro.setStep(main);
        project.addResource(macro);

        // create a step and test that calls the macro above
        StepConfiguration call_macro = new StepConfiguration(CallMacroStep.TYPE_ID);
        call_macro.addSource(CallMacroStep.ID_PARAM, ValueSourceConfiguration.forValue(macro_id));
        SteppedTest test = new SteppedTest(call_macro);

        // verify that the macro runs when the test is executed
        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        EventLog log = new EventLog();
        runner.getTestContext().addEventListener(log);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertTrue(result.isPass());
        Assert.assertNotNull("message step didn't run", log.findFirstEvent(new EventDescriptionMatcher(message)));
        }

    /**
     * Ensure parameters are passed to a function and the return value is passed back.
     */
    @Test
    public void callFunction()
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());

        // create a function to call -- it will increment the parameter value and return it
        String function_id = "TestFunction";
        Function function = new Function();
        StepConfiguration main = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        StepConfiguration store_step = new StepConfiguration(IncrementVariable.TYPE_ID);
        String param_name = "param1";
        store_step.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue(param_name));
        main.addChild(store_step);
        StepConfiguration return_step = new StepConfiguration(ReturnStep.TYPE_ID);
        return_step.addSource(ReturnStep.VALUE_PARAM, ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, param_name));
        main.addChild(return_step);
        function.setId(function_id);
        function.setStep(main);
        project.addResource(function);

        // create a step and test that calls the function above
        StepConfiguration test_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        StepConfiguration call_function = new StepConfiguration(CallFunction.TYPE_ID);
        call_function.addSource(CallFunction.ID_PARAM, ValueSourceConfiguration.forValue(function_id));
        call_function.addSource(param_name, ValueSourceConfiguration.forValue(6L));
        String return_var_id = "returned";
        call_function.addSource(CallFunction.RETURN_PARAM, ValueSourceConfiguration.forValue(return_var_id));
        test_step.addChild(call_function);

        // create a step to verify the return value
        StepConfiguration verify_step = new StepConfiguration(Verify.TYPE_ID);
        verify_step.addSource(Verify.CONDITION_PARAM, EqualityCondition.forSources(EqualityCondition.TYPE_ID, ValueSourceConfiguration.forValue(7L), ValueSourceConfiguration.forTypeWithSource(VariableValueSource.TYPE_ID, return_var_id)));
        test_step.addChild(verify_step);

        SteppedTest test = new SteppedTest(test_step);

        // verify that the return value is correct in the context (the function should have incremented by one
        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertTrue(result.isPass());
        }

    /**
     * Ensure that return exits the function immediately and following steps are not executed.
     */
    @Test
    public void returnEarlyFromFunction()
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());

        // create a function to call -- it will increment the parameter value and return it
        String function_id = "TestFunction";
        Function function = new Function();
        StepConfiguration main = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        StepConfiguration return_step = new StepConfiguration(ReturnStep.TYPE_ID);
        main.addChild(return_step);
        StepConfiguration log_step = new StepConfiguration(LogMessage.TYPE_ID);
        String logged_message = "logged";
        log_step.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue(logged_message));
        main.addChild(log_step);
        function.setId(function_id);
        function.setStep(main);
        project.addResource(function);

        // create a step and test that calls the function above
        StepConfiguration test_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        StepConfiguration call_function = new StepConfiguration(CallFunction.TYPE_ID);
        call_function.addSource(CallFunction.ID_PARAM, ValueSourceConfiguration.forValue(function_id));
        test_step.addChild(call_function);
        SteppedTest test = new SteppedTest(test_step);

        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertTrue(result.isPass());
        // verify that the message step (which comes after the return) did not run
        Assert.assertTrue(result.getLog().findEvents(new EventTypeMatcher(MuseEventType.Message)).size() == 0);
        }

    @Test
    public void emptyCompoundStep() throws StepExecutionError
        {
        StepConfiguration step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        step.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        step.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue("abc"));

        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        SteppedTest test = new SteppedTest(step);
        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertTrue(result.isPass());
        }

    @Test
    public void simpleCompoundStep() throws StepExecutionError
        {
        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        ValueSourceConfiguration condition = ValueSourceConfiguration.forType(EqualityCondition.TYPE_ID);
        condition.addSource(BinaryCondition.LEFT_PARAM, ValueSourceConfiguration.forValue(true));
        condition.addSource(BinaryCondition.RIGHT_PARAM, ValueSourceConfiguration.forValue(false));
        config.addSource(Verify.CONDITION_PARAM, condition);

        StepConfiguration main = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        main.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        main.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue("abc"));
        main.addChild(config);

        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        SteppedTest test = new SteppedTest(main);
        TestRunner runner = TestRunnerFactory.create(project, test, true, false);
        runner.runTest();
        MuseTestResult result = runner.getResult();
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        }
    }



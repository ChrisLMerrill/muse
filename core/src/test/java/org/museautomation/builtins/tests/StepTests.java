package org.museautomation.builtins.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.condition.*;
import org.museautomation.builtins.step.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class StepTests
    {
    /**
     * Ok...how do you test that something has been sent to the diagnostic log?  So this test doesn't actually test
     * the step. To do so, I think we would need to replace the logging implementation so that we could extract
     * the logged messages (perhaps with SLF4J Test: http://projects.lidalia.org.uk/slf4j-test/).  But then, how would
     * we see the rest of the logging for the unit tests?  Anyway, a human needs to inspect the log from this test to
     * see if this worked. My apologies :(
     */
    @Test
    void logMessage() throws MuseExecutionError
        {
        // this should log a message indicating there was no message parameter
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        try
            {
            MuseStep step = config.createStep();
            Assertions.fail("createStep() should have thrown an error due to a missing parameter");

            step.execute(new MockStepExecutionContext());
            }
        catch (MuseInstantiationException e)
            {
            // ok!
            }

        // this should log a message
        config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("this is the message"));
        config.createStep().execute(new MockStepExecutionContext());
        }

    @Test
    void storeStringVariable() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreVariable.TYPE_ID);
        config.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        config.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue("abc"));

        StepExecutionContext context = new MockStepExecutionContext();
        MuseStep step = config.createStep();
        step.execute(context);
        Assertions.assertEquals("abc", context.getVariable("var1"));
        }

    @Test
    void storeIntegerVariable() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreVariable.TYPE_ID);
        config.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var_int"));
        config.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue(191L));

        MuseStep step = config.createStep();
        StepExecutionContext context = new MockStepExecutionContext();
        step.execute(context);
        Assertions.assertEquals(191L, context.getVariable("var_int"));
        }

    @Test
    void storeOutput() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(StoreOutput.TYPE_ID);
        config.addSource(StoreOutput.NAME_PARAM, ValueSourceConfiguration.forValue("output1"));
        config.addSource(StoreOutput.VALUE_PARAM, ValueSourceConfiguration.forValue(111L));

        MuseStep step = config.createStep();
        StepExecutionContext context = new MockStepExecutionContext();
        step.execute(context);
        Assertions.assertEquals(111L, context.outputs().getOutput("output1"));
        }

    @Test
    void incrementVariableBy1() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(IncrementVariable.TYPE_ID);
        config.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));

        MuseStep step = config.createStep();
        StepExecutionContext context = new MockStepExecutionContext();
        context.setVariable("var1", 3L);
        step.execute(context);
        Assertions.assertEquals(4L, context.getVariable("var1"));
        }

    @Test
    void incrementVariableByN() throws MuseExecutionError
        {
        Long start_value = 101L;
        Long amount = 7L;

        StepConfiguration config = new StepConfiguration(IncrementVariable.TYPE_ID);
        config.addSource(IncrementVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        config.addSource(IncrementVariable.AMOUNT_PARAM, ValueSourceConfiguration.forValue(amount));

        MuseStep step = config.createStep();
        StepExecutionContext context = new MockStepExecutionContext();
        context.setVariable("var1", start_value);
        step.execute(context);
        Assertions.assertEquals(start_value + amount, context.getVariable("var1"));
        }

    @Test
    void verifySuccess() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        config.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(true));
        MuseStep step = config.createStep();

        StepExecutionResult result = step.execute(new MockStepExecutionContext());
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        }

    @Test
    void verifyFailed() throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        config.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false));
        MuseStep step = config.createStep();

        final MockStepExecutionContext context = new MockStepExecutionContext();
        step.execute(context);
        Assertions.assertNotNull(context.getEventLog().findEvents(new EventTypeMatcher(VerifyFailureEventType.TYPE_ID)));
        }

    @Test
    void verifyFatal() throws MuseExecutionError
        {
        verifyMaybeFatal(true);
        }

    @Test
    void verifyNotFatal() throws MuseExecutionError
        {
        verifyMaybeFatal(false);
        }

    private void verifyMaybeFatal(boolean fatal) throws MuseExecutionError
        {
        EventLogger logger = new EventLogger();
        SteppedTaskExecutionContext test_context = new DefaultSteppedTaskExecutionContext(new SimpleProject(), new SteppedTask(new StepConfiguration("mock-step")));
        test_context.addEventListener(logger);

        StepConfiguration config = new StepConfiguration(Verify.TYPE_ID);
        config.addSource(Verify.CONDITION_PARAM, ValueSourceConfiguration.forValue(false));
        if (fatal)
            config.addSource(Verify.TERMINATE_PARAM, ValueSourceConfiguration.forValue(true));
        MuseStep step = config.createStep();

        step.execute(new MockStepExecutionContext(test_context));
        Assertions.assertNotNull(logger.getLog().findEvents(new EventTypeMatcher(VerifyFailureEventType.TYPE_ID)));

        List<MuseEvent> events = logger.getLog().findEvents(new EventTypeMatcher(VerifyFailureEventType.TYPE_ID));
        Assertions.assertEquals(1, events.size());
        Assertions.assertEquals(fatal, events.get(0).hasTag(MuseEvent.TERMINATE));
        }

    @Test
    void callMacro() throws IOException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());

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
        project.getResourceStorage().addResource(macro);

        // create a step and test that calls the macro above
        StepConfiguration call_macro = new StepConfiguration(CallMacroStep.TYPE_ID);
        call_macro.addSource(CallMacroStep.ID_PARAM, ValueSourceConfiguration.forValue(macro_id));
        SteppedTask test = new SteppedTask(call_macro);

        // verify that the macro runs when the test is executed
        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(project, test);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        Assertions.assertNotNull(context.getEventLog().findFirstEvent(new EventDescriptionMatcher(message)), "message step didn't run");
        }

    /**
     * Ensure parameters are passed to a function and the return value is passed back.
     */
    @Test
    void callFunction() throws IOException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());

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
        project.getResourceStorage().addResource(function);

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

        SteppedTask test = new SteppedTask(test_step);

        // verify that the return value is correct in the context (the function should have incremented by one
        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(project, test);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        }

    /**
     * Ensure that return exits the function immediately and following steps are not executed.
     */
    @Test
    void returnEarlyFromFunction() throws IOException
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());

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
        project.getResourceStorage().addResource(function);

        // create a step and test that calls the function above
        StepConfiguration test_step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        StepConfiguration call_function = new StepConfiguration(CallFunction.TYPE_ID);
        call_function.addSource(CallFunction.ID_PARAM, ValueSourceConfiguration.forValue(function_id));
        test_step.addChild(call_function);
        SteppedTask test = new SteppedTask(test_step);

        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(project, test);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        // verify that the message step (which comes after the return) did not run
        Assertions.assertEquals(0, context.getEventLog().findEvents(new EventTypeMatcher(MessageEventType.TYPE_ID)).size());
        }

    @Test
    void emptyCompoundStep()
        {
        StepConfiguration step = new StepConfiguration(BasicCompoundStep.TYPE_ID);
        step.addSource(StoreVariable.NAME_PARAM, ValueSourceConfiguration.forValue("var1"));
        step.addSource(StoreVariable.VALUE_PARAM, ValueSourceConfiguration.forValue("abc"));

        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        SteppedTask test = new SteppedTask(step);
        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(project, test);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPass());
        }

    @Test
    void simpleCompoundStep()
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

        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        SteppedTask test = new SteppedTask(main);
        final TaskExecutionContext context = TaskRunHelper.runTaskReturnContext(project, test);
        TaskResult result = TaskResult.find(context);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isPass());
        }

    @Test
    void nullSubsourceWithDefaultValue() throws ValueSourceResolutionError
        {
        MockStep step = new MockStep();
        String result = step.getValue(null, new MockStepExecutionContext(), String.class, "default_value");
        Assertions.assertEquals("default_value", result);
        }

    class MockStep extends BaseStep
        {
        MockStep()
            {
            super(new StepConfiguration("mock"));
            }

        @Override
        public <T> T getValue(MuseValueSource source, StepExecutionContext context, Class<T> type, T default_value) throws ValueSourceResolutionError
            {
            return super.getValue(source, context, type, default_value);
            }

        @Override
        public StepExecutionResult executeImplementation(StepExecutionContext context)
            {
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        }
    }

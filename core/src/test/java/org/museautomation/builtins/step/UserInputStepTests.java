package org.museautomation.builtins.step;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.plugins.input.*;
import org.museautomation.builtins.step.userinput.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.step.*;
import org.museautomation.core.task.input.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UserInputStepTests
    {
    @Test
    public void userContinueWithDefaultMessage() throws MuseExecutionError
        {
        MuseEvent event = runStep(createContinueProvider(true, null));
        assertEquals(WaitForUserInputStep.CONTINUE_MESSAGE_DEFAULT, new UserContinueEventType().getDescription(event));
        assertNull(event.getTags());
        }

    @Test
    public void userContinueWithCustomMessage() throws MuseExecutionError
        {
        MuseEvent event = runStep(createContinueProvider(true, CUSTOM_CONTINUE_MESSAGE));
        assertEquals(CUSTOM_CONTINUE_MESSAGE, new UserContinueEventType().getDescription(event));
        assertNull(event.getTags());
        }

    @Test
    public void userAbort() throws MuseExecutionError
        {
        MuseEvent event = runStep(createContinueProvider(false, null));
        assertEquals(WaitForUserInputStep.ABORT_MESSAGE_DEFAULT, new UserContinueEventType().getDescription(event));
        assertTrue(event.hasTag(MuseEvent.TERMINATE));
        }

    @Test
    public void userAbortWithCustomMessage() throws MuseExecutionError
        {
        MuseEvent event = runStep(createContinueProvider(false, CUSTOM_ABORT_MESSAGE));
        assertEquals(CUSTOM_ABORT_MESSAGE, new UserContinueEventType().getDescription(event));
        assertTrue(event.hasTag(MuseEvent.TERMINATE));
        }

    private MuseEvent runStep(TaskInputProvider provider) throws MuseExecutionError
        {
        StepConfiguration config = new StepConfiguration(WaitForUserInputStep.TYPE_ID);
        SteppedTaskExecutionContext context = new MockSteppedTaskExecutionContext();
        context.addPlugin(new InputProviderPlugin(provider));
        context.initializePlugins();

        WaitForUserInputStep step = new WaitForUserInputStep(config, context.getProject());
        StepExecutionResult result = step.executeImplementation(new MockStepExecutionContext(context));
        assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        return context.getEventLog().findEvents(new EventTypeMatcher(UserContinueEventType.TYPE_ID)).get(0);
        }

    private TaskInputProvider createContinueProvider(boolean continue_or_abort, String message)
        {
        return new TaskInputProvider()
            {
            @Override
            public List<ResolvedTaskInput> resolveInputs(TaskInputResolutionResults resolved, UnresolvedTaskInputs inputs, MuseExecutionContext context)
                {
                List<ResolvedTaskInput> result = new ArrayList<>();
                result.add(new ResolvedTaskInput(WaitForUserInputStep.CONTINUE_INPUT_NAME, continue_or_abort, new ResolvedInputSource.InputProviderSource(this)));
                if (message != null)
                    result.add(new ResolvedTaskInput(WaitForUserInputStep.MESSAGE_INPUT_NAME, message, new ResolvedInputSource.InputProviderSource(this)));
                return result;
                }

            @Override
            public String getDescription()
                {
                return "test provider";
                }
            };
        }

    final static String CUSTOM_CONTINUE_MESSAGE = "custom continue message";
    final static String CUSTOM_ABORT_MESSAGE = "custom abort message";
    }
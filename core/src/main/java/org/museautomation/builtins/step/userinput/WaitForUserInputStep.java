package org.museautomation.builtins.step.userinput;

import org.museautomation.builtins.valuetypes.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.task.*;
import org.museautomation.core.task.input.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wait-for-user")
@MuseStepName("Wait for User")
@MuseInlineEditString("Wait for user before continuing")
@MuseStepIcon("glyph:FontAwesome:PAUSE_CIRCLE")
@MuseStepTypeGroup("User Input")
@MuseStepShortDescription("Wait for the user to confirm before continuing.")
@MuseStepLongDescription("Look for an InputProvider and asks it for two values: continue and abort-message. If continue is true, the task continues. If false, the task is aborted with the provided message (or the abort message configured for the step.")
@MuseSubsourceDescriptor(displayName = "Continue Label", description = "The label on the continue option shown to the user", type = SubsourceDescriptor.Type.Named, name = WaitForUserInputStep.CONTINUE_LABEL_PARAM, defaultValue = "Continue", optional = true)
@MuseSubsourceDescriptor(displayName = "Abort Label", description = "The label on the abort option shown to the user", type = SubsourceDescriptor.Type.Named, name = WaitForUserInputStep.ABORT_LABEL_PARAM, defaultValue = "Abort", optional = true)
@MuseSubsourceDescriptor(displayName = "Abort Message", description = "The message recorded if the user choose the abort option and the message was not returned from the InputProvider", type = SubsourceDescriptor.Type.Named, name = WaitForUserInputStep.ABORT_MESSAGE_PARAM, defaultValue = "User aborted the task", optional = true)
public class WaitForUserInputStep extends BaseStep
    {
    public WaitForUserInputStep(StepConfiguration configuration, @SuppressWarnings("unused") MuseProject project)
        {
        super(configuration);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        MuseValueSource abort_message_source = getValueSource(getConfiguration(), ABORT_MESSAGE_PARAM, false, context.getProject());

        List<TaskInput> inputs = new ArrayList<>();
        inputs.add(new TaskInput(CONTINUE_INPUT_NAME, new BooleanValueType().getId(), true));
        inputs.add(new TaskInput(MESSAGE_INPUT_NAME, new StringValueType().getId(), false));

        ResolvedTaskInput continue_input = null;
        ResolvedTaskInput message_input = null;
        List<TaskInputProvider> providers = Plugins.findAll(TaskInputProvider.class, context);
        for (TaskInputProvider provider : providers)
            {
            TaskInputResolutionResults resolved = new TaskInputResolutionResults();
            resolved.addResolvedInputs(provider.resolveInputs(resolved, new UnresolvedTaskInputs(inputs), context));
            continue_input = resolved.getResolvedInput(CONTINUE_INPUT_NAME);
            if (continue_input != null)
                {
                message_input = resolved.getResolvedInput(MESSAGE_INPUT_NAME);
                break;
                }
            }

        if (continue_input == null)
            {
            String fail_message = "No input providers could satisfy request for continuation. A TaskInputProvider should be provided by an InputProviderPlugin that can satsify the request.";
            MessageEventType.raiseError(context, fail_message);
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, fail_message);
            }
        if (!new BooleanValueType().isInstance(continue_input.getValue()))
            {
            String fail_message = String.format("The continue input must be a Boolean value. Instead it was a %s: %s", continue_input.getValue().getClass().getSimpleName(), continue_input.getValue().toString());
            MessageEventType.raiseError(context, fail_message);
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, fail_message);
            }

        String continue_message = null;
        if (message_input != null)
            continue_message = message_input.getValue().toString();

        Boolean should_continue = (Boolean) continue_input.getValue();
        if (should_continue)
            {
            if (continue_message == null)
                continue_message = CONTINUE_MESSAGE_DEFAULT;
            context.raiseEvent(UserContinueEventType.create(continue_message));
            }
        else
            {
            if (continue_message == null)
                {
                if (abort_message_source == null)
                    continue_message = ABORT_MESSAGE_DEFAULT;
                else
                    continue_message = getValue(abort_message_source, context, true, String.class);
                }
            context.raiseEvent(UserContinueEventType.createAbort(continue_message));
            }
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    // names if inputs requested from InputProvider
    public static final String CONTINUE_INPUT_NAME = "continue";
    public static final String MESSAGE_INPUT_NAME = "abort-message";

    public static final String CONTINUE_MESSAGE_DEFAULT = "User chose to continue";
    public static final String ABORT_MESSAGE_DEFAULT = "User chose to abort";

    // configuration parameters
    public static final String CONTINUE_LABEL_PARAM = "continue-label";
    public static final String ABORT_LABEL_PARAM = "abort-label";
    public static final String ABORT_MESSAGE_PARAM = "abort-message";
    public final static String TYPE_ID = WaitForUserInputStep.class.getAnnotation(MuseTypeId.class).value();
    }
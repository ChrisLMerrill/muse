package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("return")
@MuseStepName("Return")
@MuseInlineEditString("return {value}")
@MuseStepIcon("glyph:FontAwesome:\uf08b")
@MuseStepTypeGroup("Structure")
@MuseStepShortDescription("Return from a function")
@MuseStepLongDescription("Return a value to the caller and exit the current function. If the 'value' subsource is set, it will resolve that source and return it to the caller. This step only has meaning within the context of a function.")
@MuseSubsourceDescriptor(displayName = "Return value", description = "The source to resolve and return to the caller", type = SubsourceDescriptor.Type.Named, name = ReturnStep.VALUE_PARAM)
public class ReturnStep extends BaseStep
    {
    public ReturnStep(StepConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        super(configuration);
        _source = getValueSource(getConfiguration(), VALUE_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext current_context) throws MuseExecutionError
        {
        Object return_value = null;
        if (_source != null)
            return_value = getValue(_source, current_context, true, Object.class);

        // find the CallFunction step we are returning to
        StepExecutionContextStack stack = current_context.getExecutionStack();
        Iterator<StepExecutionContext> iterator = stack.iterator();
        StepConfiguration call_step_config = null;
        CallFunction call_step = null;
        while (iterator.hasNext())
            {
            StepExecutionContext context = iterator.next();
            if (context.getCurrentStepConfiguration().getType().equals(CallFunction.TYPE_ID))
                {
                call_step_config = context.getCurrentStepConfiguration();
                call_step = (CallFunction) context.getCurrentStep();
                break;
                }
            }

        if (call_step_config == null)
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "Return may only be executed within a CallFunction step.");


        // complete intermediate contexts and then pop from stack
        while (stack.peek().getCurrentStepConfiguration() != call_step_config)
            {
            StepExecutionContext popped_context = stack.peek();
            BasicStepExecutionResult result = new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            current_context.raiseEvent(EndStepEventType.create(popped_context.getCurrentStepConfiguration(), popped_context, result));
            popped_context.stepComplete(popped_context.getCurrentStep(), result);
            if (stack.peek() == popped_context)
                stack.pop();  // ensure the context was popped, if completing the step wasn't enough
            }

        // complete the CallFunction method and set the return value
        StepExecutionContext caller_context = stack.peek();
        call_step.returned(caller_context, return_value);

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("returning %s", return_value));
        }

    private MuseValueSource _source;

    public final static String VALUE_PARAM = "value";

    public final static String TYPE_ID = ReturnStep.class.getAnnotation(MuseTypeId.class).value();
    }

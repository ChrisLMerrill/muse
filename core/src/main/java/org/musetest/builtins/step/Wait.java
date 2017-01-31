package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("wait")
@MuseStepName("Wait until...")
@MuseInlineEditString("wait until {condition} is true")
@MuseStepIcon("glyph:FontAwesome:HOURGLASS_ALT")
@MuseStepShortDescription("Wait for (condition) to be true...")
@MuseStepLongDescription("The 'condition' source is resolved and evaluated as a boolean. If it is false, the step will pause for 'period' ms and then re-resolve and evaluate the source, repeating the behavior up to 'count' times. No action is taken if the condition is still false at the end.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Source to evaluate (expects a boolean)", type = SubsourceDescriptor.Type.Named, name = Wait.CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Max duration", description = "The maximum amount of time to wait for the condition to be true (in milliseconds). Default is 30 seconds", type = SubsourceDescriptor.Type.Named, name = Wait.MAX_DURATION_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Re-check frequency", description = "The amount of time to wait between re-checking the condition. Default is 500ms.", type = SubsourceDescriptor.Type.Named, name = Wait.CHECK_FREQUENCY_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Fail on false", description = "When set to true, the Wait will report a failure if the condition is still false when the maximum number of checks have been made.", type = SubsourceDescriptor.Type.Named, name = Wait.FAIL_PARAM, optional = true)
@MuseSubsourceDescriptor(displayName = "Fail message", description = "An optional message to be used when this wait fails ('Fail on false' option must be turned on).", type = SubsourceDescriptor.Type.Named, name = Wait.MESSAGE_PARAM, optional = true)
@SuppressWarnings("unused")  // instantiated via reflection
public class Wait extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public Wait(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _source = getValueSource(config, CONDITION_PARAM, true, project);
        _duration = getValueSource(config, MAX_DURATION_PARAM, false, project);
        _check_period = getValueSource(config, CHECK_FREQUENCY_PARAM, false, project);
        _fail = getValueSource(config, FAIL_PARAM, false, project);
        _message = getValueSource(config, MESSAGE_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        int wait_time = 500;  // milliseconds
        Number period_value = getValue(_check_period, context, true, Number.class);
        if (period_value != null)
            wait_time = period_value.intValue();

        int max_duration = getValue(_duration, context, Number.class, 30000L).intValue();
        boolean fail_on_false = getValue(_fail, context, Boolean.class, Boolean.FALSE);
        String fail_message = getValue(_message, context, String.class, "The condition was still false after the maximum wait was exceeded");

        boolean success = false;
        long end_time = System.currentTimeMillis() + max_duration;
        while (!success && System.currentTimeMillis() < end_time)
            {
            try
                {
                Thread.sleep(wait_time);
                }
            catch (InterruptedException e)
                {
                return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "Wait was interrupted.");
                }

            Object value = _source.resolveValue(context);
            if (!(value instanceof Boolean))
                throw new IllegalArgumentException("The source of an Wait step must resolve to a boolean value. The source (" + _source.getDescription() + ") resolved to " + value);
            success = (boolean) value;
            }

        if (!success && fail_on_false)
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, fail_message);

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _source;
    private MuseValueSource _duration;
    private MuseValueSource _check_period;
    private MuseValueSource _fail;
    private MuseValueSource _message;

    final static String CONDITION_PARAM = "condition";
    final static String MAX_DURATION_PARAM = "duration";
    final static String CHECK_FREQUENCY_PARAM = "frequency";
    final static String FAIL_PARAM = "fail";
    final static String MESSAGE_PARAM = "message";

    public final static String TYPE_ID = Wait.class.getAnnotation(MuseTypeId.class).value();
    }



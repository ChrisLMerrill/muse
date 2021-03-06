package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("verify")
@MuseStepName("Verify")
@MuseInlineEditString("verify {condition}")
@MuseStepIcon("glyph:FontAwesome:CHECK")
@MuseStepShortDescription("Check the supplied condition")
@MuseStepLongDescription("The 'condition' sub-source is resolved and evaluated as a boolean. If false, an event describing the failure is added to the test event log with a 'failure' tag.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Condition to evaluate", type = SubsourceDescriptor.Type.Named, name = Verify.CONDITION_PARAM, defaultValue = "true")
@MuseSubsourceDescriptor(displayName = "Terminate", description = "If true, terminate the test on failure", type = SubsourceDescriptor.Type.Named, name = Verify.TERMINATE_PARAM, optional = true, defaultValue = "true")
public class Verify extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public Verify(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _condition = getValueSource(config, CONDITION_PARAM, true, project);
        _terminate = getValueSource(config, TERMINATE_PARAM, false, project);
        _config = config;
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Object value = _condition.resolveValue(context);
        if (!(value instanceof Boolean))
            throw new IllegalArgumentException("The source of an Verify step must resolve to a boolean value. The source (" + _condition.getDescription() + ") resolved to " + value);

        boolean success = (Boolean) value;
        if (success)
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "verify passed");
        else
            {
            // is this step configured to terminated on verify failure?
            Boolean terminate_value = getValue(_terminate, context, true, Boolean.class);
            context.raiseEvent(VerifyFailureEventType.create(_config, String.format("Verify failure: %s is false", _condition.getDescription()), terminate_value != null && terminate_value));

            // Note that a verify that results in a failure event is still a step that executed successfully.
            // Doing otherwise (status=FAILURE results in double-counting of failures, since both the VerifyFailureEvent and the StepCompletedEvent get the failure tag.
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "verify FAILED");
            }
        }

    private MuseValueSource _condition;
    private MuseValueSource _terminate;
    private StepConfiguration _config;

    public final static String CONDITION_PARAM = "condition";
    public final static String TERMINATE_PARAM = "terminate";

    public final static String TYPE_ID = Verify.class.getAnnotation(MuseTypeId.class).value();
    }

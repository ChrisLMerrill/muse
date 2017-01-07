package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("verify")
@MuseStepName("Verify")
@MuseInlineEditString("verify {condition}")
@MuseStepIcon("glyph:FontAwesome:CHECK")
@MuseStepShortDescription("Check the supplied condition")
@MuseStepLongDescription("The 'condition' sub-source is resolved and evaluated as a boolean. If false, a VerifyFailureEvent is added to the test event log.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Condition to evaluate", type = SubsourceDescriptor.Type.Named, name = Verify.CONDITION_PARAM)
@MuseSubsourceDescriptor(displayName = "Terminate", description = "If true, terminate the test on failure", type = SubsourceDescriptor.Type.Named, name = Verify.TERMINATE_PARAM, optional = true)
public class Verify extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public Verify(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
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
        String message = String.format("%s is %b", _condition.getDescription(), success);
        if (success)
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "passed");
        else
            {
            Boolean terminate_value = getValue(_terminate, context, true, Boolean.class);
            VerifyFailureEvent event = new VerifyFailureEvent(_config, context, message);
            event.setFatal(terminate_value != null && terminate_value);
            context.raiseEvent(event);
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "FAIL");
            }
        }

    private MuseValueSource _condition;
    private MuseValueSource _terminate;
    private StepConfiguration _config;

    public final static String CONDITION_PARAM = "condition";
    public final static String TERMINATE_PARAM = "terminate";

    public final static String TYPE_ID = Verify.class.getAnnotation(MuseTypeId.class).value();
    }



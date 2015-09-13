package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("verify")
@MuseStepName("Verify")
@MuseInlineEditString("verify {condition}")
@MuseStepIcon("glyph:FontAwesome:CHECK")
public class Verify extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public Verify(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _source = getValueSource(config, CONDITION_PARAM, true, project);
        _config = config;
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
        {
        Object value = _source.resolveValue(context);
        if (!(value instanceof Boolean))
            throw new IllegalArgumentException("The source of an Verify step must resolve to a boolean value. The source (" + _source.getDescription() + ") resolved to " + value);

        boolean success = (Boolean) value;
        String message = String.format("%s is %b", _source.getDescription(), success);
        if (success)
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, "passed");
        else
            {
            context.getTestExecutionContext().raiseEvent(new VerifyFailureEvent(_config, context, message));
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, "FAIL");
            }
        }

    private MuseValueSource _source;
    private StepConfiguration _config;

    public final static String CONDITION_PARAM = "condition";

    public final static String TYPE_ID = Verify.class.getAnnotation(MuseTypeId.class).value();
    }



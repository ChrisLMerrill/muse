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
@MuseTypeId("wait")
@MuseStepName("Wait")
@MuseInlineEditString("wait for {condition}")
@MuseStepIcon("glyph:FontAwesome:HOURGLASS_ALT")
@MuseStepShortDescription("Wait for (condition)...")
@MuseStepLongDescription("The 'condition' source is resolved and evaluated as a boolean. If it is false, the step will pause for 500ms and then re-resolve and evaluate the source, repeating the behavior up to 60 times (30 seconds). No action is taken if the condition is still false at the end (but it should generate an error or fail the test!).")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Condition to evaluate", type = SubsourceDescriptor.Type.Named, name = Wait.CONDITION_PARAM)
@SuppressWarnings("unused")  // instantiated via reflection  TODO - this needs some unit tests
public class Wait extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public Wait(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config);
        _source = getValueSource(config, CONDITION_PARAM, true, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        boolean success = false;
        int count = 0;
        while (!success && count < 60)
            {
            if (count > 0)
                try
                    {
                    Thread.sleep(500);
                    }
                catch (InterruptedException e)
                    {
                    return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "Thread interrupted.");
                    }

            Object value = _source.resolveValue(context);
            if (!(value instanceof Boolean))
                throw new IllegalArgumentException("The source of an Wait step must resolve to a boolean value. The source (" + _source.getDescription() + ") resolved to " + value);
            success = (boolean) value;
            count++;
            }
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _source;

    public final static String CONDITION_PARAM = "condition";

    public final static String TYPE_ID = Wait.class.getAnnotation(MuseTypeId.class).value();
    }



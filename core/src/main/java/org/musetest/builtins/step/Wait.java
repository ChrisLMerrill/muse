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
@MuseTypeId("wait")
@MuseStepName("Wait")
@MuseStepShortDescription("Wait for (condition)...")
@MuseInlineEditString("wait for {condition}")
@MuseStepIcon("glyph:FontAwesome:HOURGLASS")
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
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
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



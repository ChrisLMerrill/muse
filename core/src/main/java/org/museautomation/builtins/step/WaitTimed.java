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
@MuseTypeId("waitsec")
@MuseStepName("Wait N seconds")
@MuseInlineEditString("wait for {duration} seconds")
@MuseStepIcon("glyph:FontAwesome:HOURGLASS_ALT")
@MuseStepShortDescription("Wait for a specific duration")
@MuseStepLongDescription("The 'duration' source is resolved and evaluated as an integer. Wait for 'duration' seconds.")
@MuseSubsourceDescriptor(displayName = "Duration", description = "How many seconds to wait (expects an integer)", type = SubsourceDescriptor.Type.Named, name = WaitTimed.DURATION_PARAM, defaultValue = "5")

@SuppressWarnings("unused,WeakerAccess")  // instantiated via reflection, public API
public class WaitTimed extends BaseStep
    {
    @SuppressWarnings("unused") // called via reflection
    public WaitTimed(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _duration = getValueSource(config, DURATION_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
        int wait_time = getValue(_duration, context, true, Number.class).intValue() * 1000;

        try
            {
            Thread.sleep(wait_time);
            }
        catch (InterruptedException e)
            {
            return new BasicStepExecutionResult(StepExecutionStatus.ERROR, "Wait was interrupted.");
            }

        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
        }

    private MuseValueSource _duration;

    public final static String DURATION_PARAM = "duration";

    public final static String TYPE_ID = WaitTimed.class.getAnnotation(MuseTypeId.class).value();
    }



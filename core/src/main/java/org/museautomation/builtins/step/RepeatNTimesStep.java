package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("repeat-n")
@MuseStepName("Repeat N times")
@MuseInlineEditString("repeat {count} times")
@MuseStepIcon("glyph:FontAwesome:REPEAT")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("Execute the child steps {count} times")
@MuseStepLongDescription("The 'count' source is resolved evaluated as an integer. The child steps will be executed 'count' times.")
@MuseSubsourceDescriptor(displayName = "Count", description = "Source to evaluate (expects an integer)", type = SubsourceDescriptor.Type.Named, name = RepeatNTimesStep.COUNT_PARAM, defaultValue = "3")
@MuseSubsourceDescriptor(displayName = "Counter varname", description = "Name of the variable to store the repeat count in", type = SubsourceDescriptor.Type.Named, name = RepeatNTimesStep.COUNT_VARNAME_PARAM, defaultValue = RepeatNTimesStep.COUNT_VARNAME_DEFAULT, optional = true)
public class RepeatNTimesStep extends BasicCompoundStep
    {
    public RepeatNTimesStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _max_count = getValueSource(config, COUNT_PARAM, false, project);
        _varname = getValueSource(config, COUNT_VARNAME_PARAM, false, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        // find the current count
        String varname = getValue(_varname, context, String.class, COUNT_VARNAME_DEFAULT);
        Object value = context.getVariable(varname);
        long repeat_count;
        if (value == null)
            repeat_count = 0;
        else if (value instanceof Number)
            repeat_count = ((Number)value).longValue();
        else
            throw new MuseExecutionError(String.format("The counter var (%s) must be a numeric value or null. Instead, it was '%s', which is a %s.", varname, value, value.getClass().getSimpleName()));

        // find the max count
        long max = getValue(_max_count, context, false, Long.class);
        if (repeat_count >= max)
            return false;

        repeat_count++;
        context.setVariable(varname, repeat_count);
        return true;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof RepeatNTimesStep
            && Objects.equals(_max_count, ((RepeatNTimesStep)obj)._max_count)
            && Objects.equals(_varname, ((RepeatNTimesStep)obj)._varname);
        }

    private MuseValueSource _max_count;
    private MuseValueSource _varname;

    public final static String COUNT_PARAM = "count";
    public final static String COUNT_VARNAME_PARAM = "counter_varname";
    public final static String COUNT_VARNAME_DEFAULT = "repeat_count";
    public final static String TYPE_ID = RepeatNTimesStep.class.getAnnotation(MuseTypeId.class).value();
    }



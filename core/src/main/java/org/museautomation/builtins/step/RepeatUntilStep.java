package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("repeat-until")
@MuseStepName("Repeat until")
@MuseInlineEditString("repeat until {condition}")
@MuseStepIcon("glyph:FontAwesome:REPEAT")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("Execute the child steps until {condition} is true")
@MuseStepLongDescription("Execute the child steps. Then evaluate the condition as a boolean - the step is complete if the condition is true. Else repeat.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Source to evaluate to determine when to stop looping (expects an boolean)", type = SubsourceDescriptor.Type.Named, name = RepeatUntilStep.CONDITION_PARAM, defaultValue = "true")
public class RepeatUntilStep extends BasicCompoundStep
    {
    public RepeatUntilStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _condition = getValueSource(config, CONDITION_PARAM, true, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws ValueSourceResolutionError
        {
        Object entered = context.getVariable(ENTERED_VARNAME);
        if (entered == null) // first time through
            {
            context.setVariable(ENTERED_VARNAME, true);
            return true;
            }
        Object object = _condition.resolveValue(context);
        if (object instanceof Boolean)
            return !((Boolean) object);
        else
            throw new IllegalArgumentException("The 'condition' parameter must resolve to a boolean value. It resolved to " + object);
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof RepeatUntilStep
            && Objects.equals(_condition, ((RepeatUntilStep)obj)._condition);
        }

    private MuseValueSource _condition;

    public final static String CONDITION_PARAM = "condition";
    public final static String ENTERED_VARNAME = "_repeat_entered";
    public final static String TYPE_ID = RepeatUntilStep.class.getAnnotation(MuseTypeId.class).value();
    }
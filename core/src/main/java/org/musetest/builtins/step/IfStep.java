package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("if")
@MuseStepName("If")
@MuseInlineEditString("if {condition}")
@MuseStepIcon("glyph:FontAwesome:QUESTION_CIRCLE")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("If (condition) is true...")
@MuseStepLongDescription("The 'condition' source is resolved evaluated as a boolean. If true, the child steps will be executed.")
public class IfStep extends BasicCompoundStep
    {
    public IfStep(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config, project);
        _condition = getValueSource(config, CONDITION_PARAM, false, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws StepExecutionError
        {
        boolean not_run_yet = super.shouldEnter(context);  // use the BasicCompoundStep's logic to only run once
        if (not_run_yet)
            {
            Object object = _condition.resolveValue(context);
            if (object instanceof Boolean)
                return (Boolean) object;
            else
                throw new IllegalArgumentException("The source of an IfStep must resolve to a boolean value. The source (" + _condition + ") resolved to " + object);
            }
        return false;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof IfStep && Objects.equals(_condition, ((IfStep)obj)._condition);
        }

    private MuseValueSource _condition;

    public final static String CONDITION_PARAM = "condition";
    public final static String TYPE_ID = IfStep.class.getAnnotation(MuseTypeId.class).value();
    }



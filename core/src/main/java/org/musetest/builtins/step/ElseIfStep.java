package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("else-if")
@MuseStepName("Else If")
@MuseInlineEditString("else if {condition}")
@MuseStepIcon("glyph:FontAwesome:QUESTION_CIRCLE")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("If (condition) is true, execute the child steps")
@MuseStepLongDescription("The 'condition' source is resolved evaluated as a boolean. If true (and all previous if conditions were false), the child steps will be executed.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Source to evaluate (expects a boolean)", type = SubsourceDescriptor.Type.Named, name = ElseIfStep.CONDITION_PARAM, defaultValue = "true")
public class ElseIfStep extends BasicCompoundStep
    {
    public ElseIfStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _condition = getValueSource(config, CONDITION_PARAM, false, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        if (super.shouldEnter(context))  // use the BasicCompoundStep's logic to only run once
            {
            _previous_entered = ElseStep.isPreviousIfEntered(context);
            if (!_previous_entered)
                {
                _entered = getValue(_condition, context, false, Boolean.class);
                return _entered;
                }
            }
        return false;
        }

    @Override
    protected StepExecutionResult createResult(StepExecutionStatus status)
        {
        StepExecutionResult result = super.createResult(status);
        result.metadata().setMetadataField(IfStep.IF_STEP_ENTERED, _entered | _previous_entered);
        return result;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof ElseIfStep && Objects.equals(_condition, ((ElseIfStep)obj)._condition);
        }

    private MuseValueSource _condition;
    private Boolean _previous_entered = false;
    private Boolean _entered = false;

    public final static String CONDITION_PARAM = "condition";
    public final static String TYPE_ID = ElseIfStep.class.getAnnotation(MuseTypeId.class).value();
    }



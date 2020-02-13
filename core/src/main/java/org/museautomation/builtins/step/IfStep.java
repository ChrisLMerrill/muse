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
@MuseTypeId("if")
@MuseStepName("If")
@MuseInlineEditString("if {condition}")
@MuseStepIcon("glyph:FontAwesome:QUESTION_CIRCLE")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("If (condition) is true, execute the child steps")
@MuseStepLongDescription("The 'condition' source is resolved evaluated as a boolean. If true, the child steps will be executed.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Source to evaluate (expects a boolean)", type = SubsourceDescriptor.Type.Named, name = IfStep.CONDITION_PARAM, defaultValue = "true")
public class IfStep extends BasicCompoundStep
    {
    public IfStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _condition = getValueSource(config, CONDITION_PARAM, false, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        if (super.shouldEnter(context))  // use the BasicCompoundStep's logic to only run once
            {
            _entered = getValue(_condition, context, false, Boolean.class);
            return _entered;
            }
        return false;
        }

    @Override
    protected StepExecutionResult createResult(StepExecutionStatus status)
        {
        StepExecutionResult result = super.createResult(status);
        result.metadata().setMetadataField(IF_STEP_ENTERED, _entered);
        return result;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!super.equals(obj))
            return false;
        return obj instanceof IfStep && Objects.equals(_condition, ((IfStep)obj)._condition);
        }

    private MuseValueSource _condition;
    private Boolean _entered;

    public final static String CONDITION_PARAM = "condition";
    public final static String IF_STEP_ENTERED = "if-entered";
    public final static String TYPE_ID = IfStep.class.getAnnotation(MuseTypeId.class).value();
    }



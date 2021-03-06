package org.museautomation.builtins.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("while")
@MuseStepName("While")
@MuseInlineEditString("while {condition}")
@MuseStepIcon("glyph:FontAwesome:REPEAT")
@MuseStepTypeGroup("Conditionals & Looping")
@MuseStepShortDescription("While (condition) is true, execute the child steps")
@MuseStepLongDescription("The 'condition' source is resolved evaluated as a boolean. If true, the child steps will be executed. After they are executed, the source will be evaluated again. This repeats until the 'condition' source resolves to false.")
@MuseSubsourceDescriptor(displayName = "Condition", description = "Source to evaluate (expects a boolean)", type = SubsourceDescriptor.Type.Named, name = WhileStep.CONDITION_PARAM, defaultValue = "true")
public class WhileStep extends BasicCompoundStep
    {
    public WhileStep(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        _condition = getValueSource(config, CONDITION_PARAM, true, project);
        }

    @Override
    protected boolean shouldEnter(StepExecutionContext context) throws MuseExecutionError
        {
        Object object = _condition.resolveValue(context);
        if (object instanceof Boolean)
            return (Boolean) object;
        else
            throw new IllegalArgumentException("The source of an WhileStep must resolve to a boolean value. The source (" + _condition + ") resolved to " + object);
        }

    private MuseValueSource _condition;

    public final static String CONDITION_PARAM = "condition";
    public final static String TYPE_ID = WhileStep.class.getAnnotation(MuseTypeId.class).value();
    }



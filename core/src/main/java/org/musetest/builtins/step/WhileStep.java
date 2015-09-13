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
@MuseTypeId("while")
@MuseStepName("While")
@MuseStepShortDescription("While (condition) is true...")
@MuseInlineEditString("while {condition}")
@MuseStepIcon("glyph:FontAwesome:REPEAT")
@MuseStepTypeGroup("Conditionals & Looping")
public class WhileStep extends BasicCompoundStep
    {
    public WhileStep(StepConfiguration config, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(config, project);
        _condition = getValueSource(config, CONDITION_PARAM, true, project);
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepConfigurationError
        {
        Object object = _condition.resolveValue(context);
        if (object instanceof Boolean)
            {
            Boolean result = (Boolean) object;
            if (result)
                return new BasicStepExecutionResult(StepExecutionStatus.INCOMPLETE);
            else
                return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE);
            }
        else
            throw new IllegalArgumentException("The source of an WhileStep must resolve to a boolean value. The source (" + _condition + ") resolved to " + object);
        }

    private MuseValueSource _condition;

    public final static String CONDITION_PARAM = "condition";
    public final static String TYPE_ID = WhileStep.class.getAnnotation(MuseTypeId.class).value();
    }



package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("return")
@MuseStepName("Return")
@MuseInlineEditString("return ${value}")
@MuseStepIcon("glyph:FontAwesome:\uf08b")
@MuseStepTypeGroup("Structure")
@MuseStepShortDescription("Return from a function")
@MuseStepLongDescription("Return a value to the caller and exit the current function. If the 'value' subsource is set, it will resolve that source and return it to the caller. This step only has meaning within the context of a function.")
public class ReturnStep extends BaseStep
    {
    public ReturnStep(StepConfiguration configuration, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        super(configuration);
        _source = getValueSource(getConfiguration(), VALUE_PARAM, false, project);
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError
        {
        Object return_value = null;
        if (_source != null)
            {
            return_value = getValue(_source, context, true, Object.class);
            context.getTestExecutionContext().setVariable(CallFunction.INTERNAL_RETURN_PARAM, return_value);
            }

        return new BasicStepExecutionResult(StepExecutionStatus.RETURN, String.format("returning %s", return_value));
        }

    private MuseValueSource _source;

    public final static String VALUE_PARAM = "value";

    public final static String TYPE_ID = ReturnStep.class.getAnnotation(MuseTypeId.class).value();
    }



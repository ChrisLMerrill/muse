package org.musetest.builtins.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("increment-variable")
@MuseStepName("Increment")
@MuseInlineEditString("increment ${name}")
@MuseStepShortDescription("Increment a variable")
@MuseStepLongDescription("Increments the value of a variable by one (or, optionally, by another value)")
@MuseStepDescriptorImplementation(IncrementVariableDescriptor.class)
@MuseStepIcon("glyph:FontAwesome:PLUS")
@MuseStepTypeGroup("Variables")
@MuseSubsourceDescriptor(displayName = "Name", description = "Name of the variable", type = SubsourceDescriptor.Type.Named, name = IncrementVariable.NAME_PARAM)
@MuseSubsourceDescriptor(displayName = "Amount", description = "Amount to increment by (default is 1)", type = SubsourceDescriptor.Type.Named, name = IncrementVariable.AMOUNT_PARAM, optional = true)
public class IncrementVariable extends BaseStep
    {
    public IncrementVariable(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _name = getValueSource(config, NAME_PARAM, true, project);
        _amount = getValueSource(config, AMOUNT_PARAM, false, project);
        }

    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError
        {
        String name = getValue(_name, context, false, String.class);
        Long amount = getValue(_amount, context, true, Long.class);
        if (amount == null)
            amount = 1L;

        Object variable = context.getVariable(name);
        if (variable == null)
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, String.format("IncrementVariable unable to proceed: the variable (%s) has not been set.", _name.getDescription()));
        if (!(variable instanceof Long))
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE, String.format("IncrementVariable unable to proceed: the variable (%s) has not been set as an integer.", _name.getDescription()));
        else
            {
            long result = (Long) variable + amount;
            context.setVariable(name, result);
            return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("%s = %d", _name.toString(), result));
            }
        }

    private MuseValueSource _name;
    private MuseValueSource _amount;

    public final static String NAME_PARAM = "name";
    public final static String AMOUNT_PARAM = "amount";
    public final static String TYPE_ID = IncrementVariable.class.getAnnotation(MuseTypeId.class).value();
    }



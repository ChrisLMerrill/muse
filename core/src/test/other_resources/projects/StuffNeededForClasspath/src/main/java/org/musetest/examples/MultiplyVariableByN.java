package org.musetest.examples;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("multiply-var-by-n")
@MuseInlineEditString("multiply ${name} by {factor}")
public class MultiplyVariableByN extends BaseStep
    {
    public MultiplyVariableByN(StepConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config);
        _name = getValueSource(config, "name", true, project);
        _factor = getValueSource(config, "factor", true, project);
        }
        
    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws ValueSourceResolutionError
        {
		String var_name = _name.resolveValue(context).toString();
		Object var_value = context.getVariable(var_name);
        if (!(var_value instanceof Number))
            {
            LOG.error(String.format("The variable value is not a number: %s=%s", var_name, var_value));
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE);
            }

        if (!(var_value instanceof Long || var_value instanceof Integer || var_value instanceof Short || var_value instanceof Byte))
            LOG.error(String.format("The value will be coverted to an integer: %s=%s", var_name, var_value));
        Long value = ((Number)var_value).longValue();

        Object factor = _factor.resolveValue(context);
        if (!(factor instanceof Number))
            {
            LOG.error(String.format("The factor did not resovle to a number: %s", factor));
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE);
            }

        if (!(var_value instanceof Long || var_value instanceof Integer || var_value instanceof Short || var_value instanceof Byte))
            LOG.error(String.format("The value will be coverted to an integer: %s=%s", var_name, var_value));

        Long new_value = value * ((Number)factor).longValue();

        context.setVariable(var_name, new_value);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("multiply %s(%d) by %d = %d", var_name, value, ((Number) factor).longValue(), new_value));
        }

    private MuseValueSource _name;
    private MuseValueSource _factor;

    final static Logger LOG = LoggerFactory.getLogger(MultiplyVariableByN.class);
    }



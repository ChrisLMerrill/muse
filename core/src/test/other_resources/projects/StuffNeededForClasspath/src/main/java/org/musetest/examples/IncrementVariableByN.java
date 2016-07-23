package org.musetest.examples;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("increment-var-by-n")
@MuseInlineEditString("increment ${name} by {amount}")
public class IncrementVariableByN extends BaseStep
    {
    public IncrementVariableByN(StepConfiguration config, MuseProject project) throws StepConfigurationError
        {
        super(config);
    	_name = getValueSource(config, "name", true, project);
    	_amount = getValueSource(config, "amount", true, project);
    	}
        
    @Override
    public StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError
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

        Object increment_value = _amount.resolveValue(context);
        if (!(increment_value instanceof Number))
            {
            LOG.error(String.format("The increment did not resovle to a number: %s", increment_value));
            return new BasicStepExecutionResult(StepExecutionStatus.FAILURE);
            }

        if (!(var_value instanceof Long || var_value instanceof Integer || var_value instanceof Short || var_value instanceof Byte))
            LOG.error(String.format("The value will be coverted to an integer: %s=%s", var_name, var_value));

        Long new_value = value + ((Number)increment_value).longValue();

        context.setVariable(var_name, new_value);
        return new BasicStepExecutionResult(StepExecutionStatus.COMPLETE, String.format("increment %s(%d) by %d = %d", var_name, value, ((Number) increment_value).longValue(), new_value));
        }

    private MuseValueSource _name;
    private MuseValueSource _amount;

    final static Logger LOG = LoggerFactory.getLogger(IncrementVariableByN.class);
    }



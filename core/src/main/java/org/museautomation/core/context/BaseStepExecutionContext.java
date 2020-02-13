package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
abstract class BaseStepExecutionContext extends DelegatesToParentExecutionContext implements StepExecutionContext
	{
	BaseStepExecutionContext(StepsExecutionContext parent_context, boolean new_variable_scope)
		{
		super(parent_context, new_variable_scope ? ContextVariableScope.Local : null);
		}

	@Override
	public abstract StepConfiguration getCurrentStepConfiguration();

	@Override
	public abstract MuseStep getCurrentStep() throws MuseInstantiationException;

	@Override
	public abstract void stepComplete(MuseStep step, StepExecutionResult result);

	@Override
	public StepExecutionContextStack getExecutionStack()
		{
		return getParent().getExecutionStack();
		}

	@Override
	public StepLocator getStepLocator()
		{
		return getParent().getStepLocator();
		}

	@Override
	public abstract boolean hasStepToExecute();

	public StepsExecutionContext getParent()
		{
		return (StepsExecutionContext) super.getParent();
		}
	}

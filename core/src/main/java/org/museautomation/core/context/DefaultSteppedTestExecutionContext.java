package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultSteppedTestExecutionContext extends DefaultTestExecutionContext implements SteppedTestExecutionContext
    {
    public DefaultSteppedTestExecutionContext(MuseExecutionContext parent, SteppedTest test)
	    {
	    super(parent, test);
	    _step_locator.loadSteps(test.getStep());
	    }

    public DefaultSteppedTestExecutionContext(MuseProject project, SteppedTest test)
	    {
	    super(new ProjectExecutionContext(project), test);
	    _step_locator.loadSteps(test.getStep());
	    }

    private Object getLocalVariable(String name)
        {
        // iterate the execution stack for the first variable scope and look for the variable there.
        Iterator<StepExecutionContext> iterator = _stack.iterator();
        while (iterator.hasNext())
            {
            StepExecutionContext context = iterator.next();
            final ContextVariableScope context_scope = context.getVariableScope();
            if (ContextVariableScope.Local.equals(context_scope))
	            return context.getVariable(name, VariableQueryScope.Local);
            }
        return null;
        }

    private boolean setLocalVariable(String name, Object value)
        {
        // iterate the execution stack for the first variable scope and set the variable there.
        Iterator<StepExecutionContext> iterator = _stack.iterator();
        while (iterator.hasNext())
	        {
	        StepExecutionContext context = iterator.next();
	        final ContextVariableScope context_scope = context.getVariableScope();
	        if (ContextVariableScope.Local.equals(context_scope))
		        {
		        context.setVariable(name, value);
		        return true;
		        }
	        }
        return false;
        }

    @Override
    public Object getVariable(String name)
        {
        Object value = getLocalVariable(name);
        if (value != null)
        	return value;

        return super.getVariable(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        if (!setLocalVariable(name, value))
        	setVariable(name, value, ContextVariableScope.Execution);
        }

    @Override
    public Object getVariable(String name, VariableQueryScope scope)
        {
        if (scope.equals(VariableQueryScope.Local))
            return getLocalVariable(name);

        return super.getVariable(name, scope);
        }

    @Override
    public void setVariable(String name, Object value, ContextVariableScope scope)
        {
        if (scope.equals(ContextVariableScope.Local))
	        {
	        setLocalVariable(name, value);
	        return;
	        }

        super.setVariable(name, value, scope);
        }

    @Override
    public StepExecutionContextStack getExecutionStack()
        {
        return _stack;
        }

    @Override
    public SteppedTest getTest()
        {
        return (SteppedTest) _test;
        }

    @Override
    public StepLocator getStepLocator()
	    {
	    return _step_locator;
	    }

    @Override
    public void raiseEvent(MuseEvent event)
	    {
	    if (event.getTypeId().equals(DynamicStepLoadingEventType.TYPE_ID))
	    	_step_locator.loadSteps(DynamicStepLoadingEventType.getLoadedSteps(event, this));
	    super.raiseEvent(event);
	    }

    private StepExecutionContextStack _stack = new StepExecutionContextStack();
    private StepLocator _step_locator = new CachedLookupStepLocator();
    }
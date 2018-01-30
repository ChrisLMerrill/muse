package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultSteppedTestExecutionContext extends BaseExecutionContext implements SteppedTestExecutionContext
    {
    public DefaultSteppedTestExecutionContext(MuseExecutionContext parent, SteppedTest test)
	    {
	    super(parent.getProject());
	    _parent_context = parent;
	    _test = test;
	    }

    public DefaultSteppedTestExecutionContext(MuseProject project, SteppedTest test)
	    {
	    super(project);
	    _parent_context = null;
	    _test = test;
	    }

    private Object getLocalVariable(String name)
        {
        // iterate the execution stack for the first variable scope and look for the variable there.
        Iterator<StepExecutionContext> iterator = _stack.iterator();
        while (iterator.hasNext())
            {
            StepExecutionContext context = iterator.next();
            Map<String, Object> variables = context.getVariables();
            if (variables != null)
                {
                Object value = variables.get(name);
                if (value != null)
                    return value;
                break;
                }
            }
        return null;
        }

    private void setLocalVariable(String name, Object value)
        {
        // iterate the execution stack for the first variable scope and set the variable there.
        Iterator<StepExecutionContext> iterator = _stack.iterator();
        while (iterator.hasNext())
            {
            StepExecutionContext context = iterator.next();
            Map<String, Object> variables = context.getVariables();
            if (variables != null)
                {
                variables.put(name,value);
                raiseEvent(SetVariableEventType.create(name, value, VariableScope.Local));
                return;
                }
            }
        }

    @Override
    public Object getVariable(String name)
        {
        Object value = getLocalVariable(name);
        if (value != null)
        	return value;

        if (_parent_context != null)
            return _parent_context.getVariable(name);

        return super.getVariable(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        setLocalVariable(name, value);
        }

    @Override
    public Object getVariable(String name, VariableScope scope)
        {
        if (scope.equals(VariableScope.Local))
            return getLocalVariable(name);

        if (_parent_context != null)
            return _parent_context.getVariable(name, scope);

        return super.getVariable(name, scope);
        }

    @Override
    public void setVariable(String name, Object value, VariableScope scope)
        {
        if (scope.equals(VariableScope.Local))
	        {
	        setLocalVariable(name, value);
	        return;
	        }

        if (_parent_context != null)
	        {
	        _parent_context.setVariable(name, value, scope);
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
        return _test;
        }

    @Override
    public StepLocator getStepLocator()
	    {
	    return _step_locator;
	    }

    private final MuseExecutionContext _parent_context;
    private StepExecutionContextStack _stack = new StepExecutionContextStack();
    private StepLocator _step_locator = new CachedLookupStepLocator();
    private final SteppedTest _test;
    }
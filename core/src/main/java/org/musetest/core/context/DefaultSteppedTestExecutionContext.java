package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultSteppedTestExecutionContext implements SteppedTestExecutionContext
    {
    public DefaultSteppedTestExecutionContext(TestExecutionContext parent_context)
        {
        _parent_context = parent_context;
        }

    @Override
    public void raiseEvent(MuseEvent event)
        {
        _parent_context.raiseEvent(event);
        }

    @Override
    public void addEventListener(MuseEventListener listener)
        {
        _parent_context.addEventListener(listener);
        }

    @Override
    public void removeEventListener(MuseEventListener listener)
        {
        _parent_context.removeEventListener(listener);
        }

    @Override
    public Object getVariable(String name)
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

        // if the local scope has not defined the variable, look at the test scope
        return _parent_context.getVariable(name);
        }

    @Override
    public void setVariable(String name, Object value)
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
                return;
                }
            }

        _parent_context.setVariable(name, value);
        }

    @Override
    public MuseProject getProject()
        {
        return _parent_context.getProject();
        }

    @Override
    public void registerShuttable(Shuttable shuttable)
        {
        _parent_context.registerShuttable(shuttable);
        }

    @Override
    public void cleanup()
        {
        _parent_context.cleanup();
        }

    @Override
    public StepExecutionContextStack getExecutionStack()
        {
        return _stack;
        }

    @Override
    public void setExecutionStack(StepExecutionContextStack stack)
        {
        _stack = stack;
        }

    private TestExecutionContext _parent_context;
    private StepExecutionContextStack _stack = new StepExecutionContextStack();
    }



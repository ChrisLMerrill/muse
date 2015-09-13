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
        if (!_variable_scopes.isEmpty())
            {
            Object value = _variable_scopes.peek().get(name);
            if (value != null)
                return value;
            }
        return _parent_context.getVariable(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        if (_variable_scopes.isEmpty())
            _parent_context.setVariable(name, value);
        else
            _variable_scopes.peek().put(name, value);
        }

    @Override
    public StepConfigProvider getStepConfigProvider()
        {
        return _provider;
        }

    public void pushProvider(StepConfigProvider provider)
        {
        _provider.pushProvider(provider);
        }

    @Override
    public void pushNewVariableScope()
        {
        _variable_scopes.push(new HashMap<>());
        }

    @Override
    public void popVariableScope()
        {
        _variable_scopes.pop();
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

    private StepProviderStack _provider = new StepProviderStack();
    private Stack<Map<String, Object>> _variable_scopes = new Stack<>();
    private TestExecutionContext _parent_context;
    }



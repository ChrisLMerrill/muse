package org.musetest.core.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockStepExecutionContext implements StepExecutionContext
    {
    public MockStepExecutionContext()
        {
        }

    public MockStepExecutionContext(MuseProject project)
        {
        DefaultTestExecutionContext parent_context = new DefaultTestExecutionContext(project, null);
        _test_context = new DefaultSteppedTestExecutionContext(parent_context);
        }

    public MockStepExecutionContext(TestExecutionContext test_context)
        {
        _test_context = new DefaultSteppedTestExecutionContext(test_context);
        }

    private SteppedTestExecutionContext _test_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(new SimpleProject(), null));

    @Override
    public StepConfiguration getCurrentStepConfiguration()
        {
        return null;
        }

    @Override
    public MuseStep getCurrentStep() throws MuseInstantiationException
        {
        return null;
        }

    @Override
    public void stepComplete(MuseStep step, StepExecutionResult result)
        {
        }

    @Override
    public boolean hasStepToExecute()
        {
        return false;
        }

    @Override
    public Map<String, Object> getVariables()
        {
        return _variables;
        }

    @Override
    public Object getVariable(String name)
        {
        return _variables.get(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        _variables.put(name, value);
        }

    @Override
    public Object getVariable(String name, VariableScope scope)
        {
        return _variables.get(name);
        }

    @Override
    public void setVariable(String name, Object value, VariableScope scope)
        {
        _variables.put(name, value);
        }

    @Override
    public void raiseEvent(MuseEvent event)
        {
        _test_context.raiseEvent(event);
        }

    @Override
    public MuseProject getProject()
        {
        return _test_context.getProject();
        }

    @Override
    public StepExecutionContextStack getExecutionStack()
        {
        return _test_context.getExecutionStack();
        }

    @Override
    public void registerShuttable(Shuttable shuttable)
        {
        _test_context.registerShuttable(shuttable);
        }

    @Override
    public SteppedTestExecutionContext getParent()
        {
        return _test_context;
        }

    @Override
    public void addEventListener(MuseEventListener listener)
        {
        _test_context.addEventListener(listener);
        }

    @Override
    public void removeEventListener(MuseEventListener listener)
        {
        _test_context.removeEventListener(listener);
        }

    @Override
    public void cleanup() {}

    @Override
    public void addInitializer(ContextInitializer initializer) {}

    @Override
    public void runInitializers() throws MuseExecutionError {}

    private Map<String, Object> _variables = new HashMap<>();
    }


package org.musetest.core.context;

import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleStepExecutionContext implements StepExecutionContext
    {
    public SimpleStepExecutionContext(SteppedTestExecutionContext test_context)
        {
        _test_context = test_context;
        }

    @Override
    public SteppedTestExecutionContext getTestExecutionContext()
        {
        return _test_context;
        }

    @Override
    public Object getStepVariable(String name)
        {
        return _step_vars.get(name);
        }

    @Override
    public void setStepVariable(String name, Object value)
        {
        _step_vars.put(name, value);
        }

    private SteppedTestExecutionContext _test_context;
    private Map<String, Object> _step_vars = new HashMap<>();

    // convenience method for unit tests
    public static SimpleStepExecutionContext create()
        {
        return new SimpleStepExecutionContext(new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext()));
        }

    // convenience method for unit tests
    public static SimpleStepExecutionContext create(MuseProject project)
        {
        return new SimpleStepExecutionContext(new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(project)));
        }
    }



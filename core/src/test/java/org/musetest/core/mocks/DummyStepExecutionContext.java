package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class  DummyStepExecutionContext implements StepExecutionContext
    {
    public DummyStepExecutionContext()
        {
        }

    public DummyStepExecutionContext(MuseProject project)
        {
        _test_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(project));
        }

    @Override
    public SteppedTestExecutionContext getTestExecutionContext()
        {
        return _test_context;
        }

    private SteppedTestExecutionContext _test_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext());

    @Override
    public Object getStepVariable(String name)
        {
        return null;
        }

    @Override
    public void setStepVariable(String name, Object value)
        {

        }
    }



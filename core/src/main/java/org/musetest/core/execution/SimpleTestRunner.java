package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SimpleTestRunner extends BaseTestRunner
    {
    public SimpleTestRunner(MuseProject project, MuseTest test, TestExecutionContext context)
        {
        _project = project;
        _test = test;
        if (context == null)
            {
            if (test instanceof SteppedTest)
                _context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(project, test));
            else
                _context = new DefaultTestExecutionContext(project, test);
            }
        else
            _context = context;
        }

    @Override
    public void runTest()
        {
        setTestResult(_test.execute(_context));
        }

    @Override
    public void requestStop()
        {
        throw new IllegalStateException("This kind of test runner can't be stopped externally");
        }
    }



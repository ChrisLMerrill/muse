package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class SimpleTestRunner implements TestRunner
    {
    @SuppressWarnings("WeakerAccess")  // part of public API
    public SimpleTestRunner(MuseProject project, MuseTest test, TestExecutionContext context)
        {
        if (project == null)
            throw new IllegalArgumentException("project is required");
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
    public TestExecutionContext getExecutionContext()
        {
        return _context;
        }

    @Override
    public MuseTestResult getResult()
        {
        return _result;
        }

    protected void setTestResult(MuseTestResult result)
        {
        _result = result;
        }

    protected MuseTest _test;
    protected MuseProject _project;
    protected TestExecutionContext _context;
    private MuseTestResult _result;
    }



package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
abstract class BaseTestRunner implements TestRunner
    {
    @Override
    public TestExecutionContext getTestContext()
        {
        return _context;
        }

    @Override
    public MuseTestResult getResult()
        {
        return _result;
        }

    void setTestResult(MuseTestResult result)
        {
        _result = result;
        }

    protected MuseTest _test;
    protected MuseProject _project;
    TestExecutionContext _context;
    private MuseTestResult _result;
    }



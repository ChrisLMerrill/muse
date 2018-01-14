package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugin.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class SimpleTestRunner implements TestRunner
    {
    @SuppressWarnings("WeakerAccess")  // part of public API
    public SimpleTestRunner(MuseProject project, MuseTest test)
        {
        _project = project;
        _config = new TestHolder(test, TestExecutionContextFactory.create(project, test));
        }

    public SimpleTestRunner(MuseProject project, TestConfiguration config)
	    {
	    _project = project;
	    _config = config;
	    }

    public SimpleTestRunner(TestExecutionContext context)
        {
        _config = new TestHolder(context.getTest(), context);
        }

    @Override
    public void runTest()
        {
        startTest();
        _context.raiseEvent(StartTestEventType.create(_test));
        setTestResult(_config.test().execute(_config.context()));
        _context.raiseEvent(EndTestEventType.create(getResult().getOneLineDescription(), getResult().isPass()));
        finishTest();
        }

    protected void startTest()
	    {
	    _config.withinProject(_project);
	    _test = _config.test();
	    _context = _config.context();
	    try
		    {
		    TestPlugins.setup(_context);
		    for (TestPlugin plugin : _config.plugins())
			    _context.addTestPlugin(plugin);
		    _context.initializePlugins(null);
		    }
	    catch (MuseExecutionError e)
		    {
		    final String message = "Unable to setup the test plugins due to: " + e.getMessage();
		    LOG.error(message);
		    this._context.raiseEvent(TestErrorEventType.create(message));
		    setTestResult(new BaseMuseTestResult(_test, _context, new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, message)));
		    }
	    }

    protected void finishTest()
	    {
	    try
	        {
	        _config.context().cleanup();
	        }
	    catch (Throwable e)
	        {
	        LOG.error("Exception during test cleanup:", e);
	        // regardless of exception here, we want to return normally.
	        }
	    }

    @Override
    public TestExecutionContext getExecutionContext()
        {
        return _config.context();
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

    private TestConfiguration _config;
    protected MuseProject _project;
    protected TestExecutionContext _context;
    protected MuseTest _test;
    private MuseTestResult _result;


    private class TestHolder implements TestConfiguration
	    {
	    public TestHolder(MuseTest test, TestExecutionContext context)
		    {
		    _test = test;
		    _context = context;
		    }

	    @Override
	    public void withinProject(MuseProject project)
		    {

		    }

	    @Override
	    public MuseTest test()
		    {
		    return _test;
		    }

	    @Override
	    public String name()
		    {
		    return null;
		    }

	    @Override
	    public TestExecutionContext context()
		    {
		    return _context;
		    }

	    @Override
	    public List<TestPlugin> plugins()
		    {
		    return Collections.emptyList();
		    }

	    @Override
	    public void addPlugin(TestPlugin plugin)
		    {

		    }

	    MuseTest _test;
	    TestExecutionContext _context;
	    }

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTestRunner.class);
    }

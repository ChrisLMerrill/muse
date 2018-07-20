package org.musetest.core.execution;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.test.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess")  // part of public API
public class SimpleTestRunner implements TestRunner
    {
    public SimpleTestRunner(MuseExecutionContext context, TestConfiguration config)
	    {
	    _context = context;
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
        _completed_normally = _config.test().execute(_config.context());
        finishTest();
        }

    @Override
    public Boolean completedNormally()
	    {
	    return _completed_normally;
	    }

    protected void startTest()
	    {
	    _config.withinContext(_context);
	    _test = _config.test();
	    _test_context = _config.context();
	    try
		    {
		    for (MusePlugin plugin : _config.plugins())
			    _test_context.addPlugin(plugin);
		    Plugins.setup(_test_context);
		    _test_context.initializePlugins();
		    }
	    catch (MuseExecutionError e)
		    {
		    final String message = "Unable to setup the test plugins due to: " + e.getMessage();
		    LOG.error(message);
		    this._test_context.raiseEvent(TestErrorEventType.create(message));
		    }
	    _test_context.raiseEvent(StartTestEventType.create(_test.getId(), _config.name()));
	    }

    protected void finishTest()
	    {
	    _test_context.raiseEvent(EndTestEventType.create());
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

    private TestConfiguration _config;
    protected MuseExecutionContext _context;
    protected TestExecutionContext _test_context;
    protected MuseTest _test;
    protected Boolean _completed_normally;

    private class TestHolder implements TestConfiguration
	    {
	    public TestHolder(MuseTest test, TestExecutionContext context)
		    {
		    _test = test;
		    _context = context;
		    }

	    @Override
	    public void withinContext(MuseExecutionContext parent_context)
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
		    return _test.getDescription();
		    }

	    @Override
	    public TestExecutionContext context()
		    {
		    return _context;
		    }

	    @Override
	    public void addPlugin(MusePlugin plugin)
		    {
		    }

	    @Override
	    public List<MusePlugin> plugins()
		    {
		    return Collections.emptyList();
		    }

	    @Override
	    public void addContextCreationListener(ContextCreationListener listener)
		    {
		    listener.contextCreated(_context);
		    }

	    @Override
	    public void removeContextCreationListener(ContextCreationListener listener)
		    {

		    }

	    MuseTest _test;
	    TestExecutionContext _context;
	    }

    private final static Logger LOG = LoggerFactory.getLogger(SimpleTestRunner.class);
    }

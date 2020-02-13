package org.museautomation.core.test;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * Everything needed to run a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BasicTestConfiguration implements TestConfiguration
    {
    public BasicTestConfiguration(String test_id)
        {
        _test_id = test_id;
        }

    public BasicTestConfiguration(MuseTest test)
	    {
	    _test = test;
	    }

    public MuseTest test()
        {
        if (_test == null && _parent_context != null)
	        {
	        _test = _parent_context.getProject().getResourceStorage().getResource(_test_id, MuseTest.class);
	        if (_test == null)  // not found in project
	        	_test = new MissingTest(_test_id);
	        }
        return _test;
        }

    public String name()
        {
        if (_name != null)
            return _name;
        return test().getDescription();
        }

    @Override
    public void withinContext(MuseExecutionContext parent_context)
	    {
	    if (_parent_context == null)
	        _parent_context = parent_context;
	    else if (!_parent_context.equals(parent_context))
	    	throw new IllegalArgumentException("Cannot use a config within a 2nd context");
	    }

    public TestExecutionContext context()
	    {
	    if (_context == null)
	    	_context = TestExecutionContextFactory.create(_parent_context, test());
	    return _context;
	    }

    public String getName()
	    {
	    return name();
	    }
    public void setName(String name)
        {
        _name = name;
        }

    public List<MusePlugin> plugins()
        {
        if (_plugins == null)
        	return Collections.emptyList();
        return Collections.unmodifiableList(_plugins);
        }

    public void addPlugin(MusePlugin plugin)
	    {
    	if (_plugins == null)
    		_plugins = new ArrayList<>();
    	_plugins.add(plugin);
        }

    @Override
    public String toString()
        {
        return _name;
        }

    // these are configurations that would be persisted
    private String _test_id;
    private String _name;
    private List<MusePlugin> _plugins;

    // these are set/cached for use at test execution time
    private transient MuseExecutionContext _parent_context;
    private MuseTest _test;
    private TestExecutionContext _context;
    }
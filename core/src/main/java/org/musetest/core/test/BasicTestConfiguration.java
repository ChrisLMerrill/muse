package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.test.plugin.*;

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
        if (_test == null && _project != null)
	        {
	        _test = _project.getResourceStorage().getResource(_test_id, MuseTest.class);
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
    public void withinProject(MuseProject project)
	    {
	    if (_project == null)
	        _project = project;
	    else if (_project != project)
	    	throw new IllegalArgumentException("Cannot use a config within a different project");
	    }

    public TestExecutionContext context()
	    {
	    if (_context == null)
	    	_context = TestExecutionContextFactory.create(_project, test());
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

    public List<TestPlugin> plugins()
        {
        if (_plugins == null)
        	return Collections.emptyList();
        return Collections.unmodifiableList(_plugins);
        }

    public void addPlugin(TestPlugin plugin)
	    {
    	if (_plugins == null)
    		_plugins = new ArrayList<>();
    	_plugins.add(plugin);
        }

    // these are configurations that would be persisted
    private String _test_id;
    private String _name;
    private List<TestPlugin> _plugins;

    // these are set/cached for use at test execution time
    private transient MuseProject _project;
    private MuseTest _test;
    private TestExecutionContext _context;
    }
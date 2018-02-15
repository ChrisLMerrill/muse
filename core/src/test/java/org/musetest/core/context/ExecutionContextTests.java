package org.musetest.core.context;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExecutionContextTests
	{
	@Test
	public void createVariable()
	    {
	    String varname = _context.createVariable("varprefix-", "value");
	    Assert.assertTrue(varname.startsWith("varprefix-"));
	    Assert.assertEquals("value", _context.getVariable(varname));
	    }

	@Test
	public void createVariableTwice()
	    {
	    String varname1 = _context.createVariable("varprefix-", "value1");
	    String varname2 = _context.createVariable("varprefix-", "value2");
	    Assert.assertNotEquals(varname1, varname2);
	    Assert.assertEquals("value1", _context.getVariable(varname1));
	    Assert.assertEquals("value2", _context.getVariable(varname2));
	    }

	@Before
	public void setup()
		{
		_project = new SimpleProject();
		_context = new BaseExecutionContext(_project);
		}

	private MuseExecutionContext _context;
	private MuseProject _project;
	}



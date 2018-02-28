package org.musetest.core.context;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ExecutionContextTests
	{
	@Test
	public void receiveEvents()
	    {
	    List<MuseEvent> events_received = new ArrayList<>();
	    MuseEventListener listener = events_received::add;

	    ProjectExecutionContext context = new ProjectExecutionContext(_project);
	    context.addEventListener(listener);
	    context.raiseEvent(MessageEventType.create("message1"));

	    Assert.assertEquals(1, events_received.size());
	    Assert.assertEquals(MessageEventType.TYPE_ID, events_received.get(0).getTypeId());

	    context.removeEventListener(listener);
	    context.raiseEvent(MessageEventType.create("message2"));
	    events_received.clear();
	    Assert.assertEquals(0, events_received.size());
	    }

	@Test
	public void getProject()
	    {
	    ProjectExecutionContext context = new ProjectExecutionContext(_project);
	    Assert.assertTrue(_project == context.getProject());

	    // TODO test for other contexts
	    }

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

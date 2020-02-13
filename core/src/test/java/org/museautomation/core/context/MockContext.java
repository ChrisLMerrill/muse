package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.project.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class MockContext extends BaseExecutionContext
	{
	MockContext()
		{
		super(new SimpleProject(), ContextVariableScope.Project);
		}

	@Override
	public synchronized void raiseEvent(MuseEvent event)
		{
		_events_raised.add(event);
		super.raiseEvent(event);
		}

	List<MuseEvent> _events_raised = new ArrayList<>();
	}



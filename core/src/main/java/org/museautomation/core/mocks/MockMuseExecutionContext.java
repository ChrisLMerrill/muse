package org.museautomation.core.mocks;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;

import java.util.*;

/**
 * Provides testability functions on top of the base implementation
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // public APIs for testing in other packages
public class MockMuseExecutionContext extends BaseExecutionContext
	{
	public MockMuseExecutionContext()
		{
		super(new SimpleProject(), ContextVariableScope.Project);
		}

	public List<MuseEventListener> getListeners()
		{
		return _listeners;
		}
	}

package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;

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
		super(new SimpleProject());
		}

	public List<MuseEventListener> getListeners()
		{
		return _listeners;
		}
	}



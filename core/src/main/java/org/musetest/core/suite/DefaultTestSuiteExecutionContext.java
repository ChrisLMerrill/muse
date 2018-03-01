package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestSuiteExecutionContext extends BaseExecutionContext implements TestSuiteExecutionContext
	{
	public DefaultTestSuiteExecutionContext(MuseProject project, MuseTestSuite suite)
		{
		super(project, ContextVariableScope.Suite);
		_suite = suite;
		}

	public DefaultTestSuiteExecutionContext(MuseExecutionContext parent, MuseTestSuite suite)
		{
		super(parent, ContextVariableScope.Suite);
		_suite = suite;
		}

	@Override
	public MuseTestSuite getSuite()
		{
		return _suite;
		}

	private MuseTestSuite _suite;
	}

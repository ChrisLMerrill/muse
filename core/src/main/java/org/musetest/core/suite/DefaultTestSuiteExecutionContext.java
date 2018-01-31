package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestSuiteExecutionContext extends BaseExecutionContext implements TestSuiteExecutionContext
	{
	DefaultTestSuiteExecutionContext(MuseProject project, MuseTestSuite suite)
		{
		super(project);
		_suite = suite;
		}

	@Override
	public MuseTestSuite getSuite()
		{
		return _suite;
		}

	private MuseTestSuite _suite;
	}



package org.musetest.core.context;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestExecutionContext extends BaseExecutionContext implements TestExecutionContext
	{
	public DefaultTestExecutionContext(MuseProject project, MuseTest test)
		{
		super(project);
		_test = test;
		}

	@Override
	public MuseTest getTest()
		{
		return _test;
		}

	private final MuseTest _test;
	}

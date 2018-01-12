package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.test.plugins.*;
import org.slf4j.*;

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

	private final static Logger LOG = LoggerFactory.getLogger(DefaultTestExecutionContext.class);
	}



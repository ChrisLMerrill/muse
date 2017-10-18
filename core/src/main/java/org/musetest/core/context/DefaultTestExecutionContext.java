package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.context.initializers.*;
import org.musetest.core.datacollection.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestExecutionContext extends BaseExecutionContext implements TestExecutionContext
	{
	public DefaultTestExecutionContext(MuseProject project, MuseTest test)
		{
		super(project);
		_test = test;

		try
			{
			ContextInitializers.setup(this);
			}
		catch (MuseExecutionError e)
			{
			LOG.error("Unable to setup ContextInitializers due to: " + e.getMessage());
			}
		addInitializer(new TestDefaultsInitializer(this));
		}

	@Override
	public MuseTest getTest()
		{
		return _test;
		}

	private final MuseTest _test;

	private final static Logger LOG = LoggerFactory.getLogger(DefaultTestExecutionContext.class);
	}



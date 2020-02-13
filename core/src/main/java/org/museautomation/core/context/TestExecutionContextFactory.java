package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestExecutionContextFactory
	{
	public static TestExecutionContext create(MuseExecutionContext context, MuseTest test)
		{
		if (test instanceof SteppedTest)
			return new DefaultSteppedTestExecutionContext(context, (SteppedTest) test);
		return new DefaultTestExecutionContext(context, test);
		}
	}

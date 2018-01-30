package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestExecutionContextFactory
	{
	public static TestExecutionContext create(MuseProject project, MuseTest test)
		{
		if (test instanceof SteppedTest)
			return new DefaultSteppedTestExecutionContext(project, (SteppedTest) test);
		return new DefaultTestExecutionContext(project, test);
		}
	}

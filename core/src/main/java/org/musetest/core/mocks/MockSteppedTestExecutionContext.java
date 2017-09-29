package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockSteppedTestExecutionContext extends MockStepExecutionContext implements SteppedTestExecutionContext
	{
	@Override
	public MuseTest getTest()
		{
		return null;
		}
	}



package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockSteppedTestExecutionContext extends MockStepExecutionContext implements SteppedTestExecutionContext
	{
	public MockSteppedTestExecutionContext(MuseTest test)
		{
		super(test);
		}

	public MockSteppedTestExecutionContext()
		{
		super(new MockTest());
		}

	@Override
	public MuseTest getTest()
		{
		return _test_context.getTest();
		}
	}



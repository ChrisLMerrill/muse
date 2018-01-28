package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;

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
		super(new SteppedTest(new StepConfiguration("mock-step")));
		}

	@Override
	public SteppedTest getTest()
		{
		return _test_context.getTest();
		}
	}



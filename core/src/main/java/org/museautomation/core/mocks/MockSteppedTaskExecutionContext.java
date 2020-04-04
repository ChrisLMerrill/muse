package org.museautomation.core.mocks;

import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockSteppedTaskExecutionContext extends MockStepExecutionContext implements SteppedTaskExecutionContext
	{
	public MockSteppedTaskExecutionContext(SteppedTask task)
		{
		super(task);
		}

	public MockSteppedTaskExecutionContext()
		{
		super(new SteppedTask(new StepConfiguration("mock-step")));
		}

	@Override
	public String getTaskExecutionId()
		{
		return getTask().getId();
		}
    
	@Override
	public SteppedTask getTask()
		{
		return _test_context.getTask();
		}
	}



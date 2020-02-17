package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskSuiteExecutionContext extends MuseExecutionContext
	{
	MuseTaskSuite getSuite();

	String getTaskExecutionId(DefaultTaskExecutionContext task_context);
	}



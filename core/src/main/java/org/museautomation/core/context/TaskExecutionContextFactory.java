package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.steptask.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskExecutionContextFactory
	{
	public static TaskExecutionContext create(MuseExecutionContext context, MuseTask task)
		{
		if (task instanceof SteppedTask)
			return new DefaultSteppedTaskExecutionContext(context, (SteppedTask) task);
		return new DefaultTaskExecutionContext(context, task);
		}
	}

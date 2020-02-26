package org.museautomation.core.context;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskExecutionContext extends MuseExecutionContext
    {
    MuseTask getTask();
    String getTaskExecutionId();

    static MuseTask findTask(MuseExecutionContext context)
        {
        while (context != null)
            {
            if (context instanceof TaskExecutionContext)
                return ((TaskExecutionContext)context).getTask();
            context = context.getParent();
            }
        return null;
        }
    }
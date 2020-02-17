package org.museautomation.core.task;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseMuseTask extends BaseMuseResource implements MuseTask
    {
    @Override
    public boolean execute(TaskExecutionContext context)
        {
        try
            {
            return executeImplementation(context);
            }
        catch (Throwable e)
            {
            MuseEvent event = TaskErrorEventType.create("An exception was thrown: " + e.getMessage());
            context.raiseEvent(event);
            return false;
            }
        }

    @Override
    public String getDescription()
        {
        return getId();
        }

    protected abstract boolean executeImplementation(TaskExecutionContext context);

    @Override
    public ResourceType getType()
        {
        return new MuseTask.TaskResourceType();
        }
    }
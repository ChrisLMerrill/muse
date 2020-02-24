package org.museautomation.core.task;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MissingTask extends BaseMuseTask
    {
    public MissingTask(String id)
        {
        _id = id;
        }

    @Override
    public boolean executeImplementation(TaskExecutionContext context)
        {
        final MuseEvent event = StartTaskEventType.create(getId(), getDescription());
        event.addTag(MuseEvent.ERROR);
        context.raiseEvent(event);
        return false;
        }

    @Override
    public String getDescription()
        {
        return "MissingTest (id=" + _id + ")";
        }

    @Override
    public Map<String, ValueSourceConfiguration> getDefaultVariables()
        {
        return null;
        }

    @Override
    public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
        {

        }

    @Override
    public void setDefaultVariable(String name, ValueSourceConfiguration source)
        {

        }

    @Override
    public ResourceType getType()
        {
        return new MuseTask.TaskResourceType();
        }

    private String _id;
    }
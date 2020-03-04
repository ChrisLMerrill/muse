package org.museautomation.core.task;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.task.state.*;

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
    public TaskInputSet getInputSet()
        {
        if (_inputs == null)
            _inputs = new TaskInputSet();
        return _inputs;
        }

    public void setInputs(TaskInputSet inputs)
        {
        _inputs = inputs;
        }

    @Override
    public TaskOutputSet getOutputSet()
        {
        return _outputs;
        }

    public void setOutputs(TaskOutputSet outputs)
        {
        _outputs = outputs;
        }

    public TaskInputStates getInputStates()
        {
        return _in_states;
        }

    public void setInputStates(TaskInputStates in_states)
        {
        _in_states = in_states;
        }

    public TaskOutputStates getOutputStates()
        {
        return _out_states;
        }

    public void setOutputStates(TaskOutputStates out_states)
        {
        _out_states = out_states;
        }

    @Override
    public ResourceType getType()
        {
        return new MuseTask.TaskResourceType();
        }

    private TaskInputSet _inputs = new TaskInputSet();
    private TaskOutputSet _outputs = new TaskOutputSet();
    private TaskInputStates _in_states = new TaskInputStates();
    private TaskOutputStates _out_states = new TaskOutputStates();
    }
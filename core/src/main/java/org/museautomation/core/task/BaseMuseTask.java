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

    public void setInputSet(TaskInputSet inputs)
        {
        _inputs = inputs;
        }

    @Deprecated  // temporarily kept for serialization compatability
    public TaskInputSet getInputs()
        {
        return null;
        }
    @Deprecated  // temporarily kept for serialization compatability
    public void setInputs(TaskInputSet inputs)
        {
        if (inputs != null && inputs.getList().size() > 0)
            _inputs = inputs;
        }

    @Override
    public TaskOutputSet getOutputSet()
        {
        return _outputs;
        }

    public void setOutputSet(TaskOutputSet outputs)
        {
        _outputs = outputs;
        }

    @Deprecated  // temporarily kept for serialization compatability
    public TaskOutputSet getOutputs()
        {
        return null;
        }
    @Deprecated  // temporarily kept for serialization compatability
    public void setOutputs(TaskOutputSet outputs)
        {
        if (outputs != null && outputs.getList().size() > 0)
            _outputs = outputs;
        }

    @Deprecated
    public TaskInputStates getInputStates() { return null; }
    @Deprecated
    @SuppressWarnings("unused")
    public void setInputStates(TaskInputStates in_states) { }
    @Deprecated
    public TaskOutputStates getOutputStates() { return null; }
    @Deprecated
    @SuppressWarnings("unused")
    public void setOutputStates(TaskOutputStates out_states) { }

    @Override
    public ResourceType getType()
        {
        return new MuseTask.TaskResourceType();
        }

    private TaskInputSet _inputs = new TaskInputSet();
    private TaskOutputSet _outputs = new TaskOutputSet();
    }
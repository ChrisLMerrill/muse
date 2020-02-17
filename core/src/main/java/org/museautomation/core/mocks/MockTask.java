package org.museautomation.core.mocks;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.task.*;
import org.museautomation.core.values.*;
import org.museautomation.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTask extends BaseMuseTask
    {
    public MockTask()
        {
        setId("mock-test");
        }

    public MockTask(String id)
        {
        setId(id);
        }

    public MockTask(MuseTaskFailureDescription failure)
        {
        _failure = new TaskResult.Failure(TaskResult.FailureType.valueOf(failure.getFailureType().name()), failure.getReason());
        }

    public MockTask(TaskResult.Failure failure)
        {
        _failure = failure;
        }

    public MockTask(MuseTaskFailureDescription failure, String id)
        {
        this(failure);
        setId(id);
        }

    @Override
    protected boolean executeImplementation(TaskExecutionContext context)
        {
        context.raiseEvent(StartTaskEventType.create(getId(), getId()));
        if (_failure != null)
	        {
	        final MuseEvent event = MessageEventType.create(_failure.getDescription());
	        switch (_failure.getType())
		        {
		        case Error:
			        event.addTag(MuseEvent.ERROR);
			        break;
		        case Failure:
			        event.addTag(MuseEvent.FAILURE);
			        break;
		        }
	        context.raiseEvent(event);
	        }
        context.raiseEvent(EndTaskEventType.create());
        return true;
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

    private TaskResult.Failure _failure;
    }



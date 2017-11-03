package org.musetest.core.events;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepEvent extends MuseEvent
    {
    public StepEvent(EventType type, StepConfiguration config, StepExecutionContext context)
        {
        this(type, config, context, null);
        }

    public StepEvent(EventType type, StepConfiguration config, StepExecutionContext context, StepExecutionResult result)
        {
        super(type);
        _config = config;
        final Object id = config.getMetadataField(StepConfiguration.META_ID);
        if (id != null && id instanceof Number)
	        _stepid = ((Number) id).longValue();
        _step_description = context.getProject().getStepDescriptors().get(config).getShortDescription(config);
        _result = result;
        }

    @Override
    public String getDescription()
        {
        StringBuilder builder = new StringBuilder();
        if (getTypeId().equals(StartStepEventType.TYPE_ID))
            builder.append("Starting:");
        else if (getTypeId().equals(EndStepEventType.TYPE_ID))
            builder.append("Finished:");
        else
            builder.append(getType().getName());

        builder.append(" ");
        builder.append(_step_description);

        if (_result != null && !(StepExecutionStatus.COMPLETE.equals(_result.getStatus())))
            {
            builder.append(": ");
            builder.append(_result.getDescription());
            }

        return builder.toString();
        }

    public Long getStepId()
        {
        return _stepid;
        }

    public StepExecutionResult getResult()
        {
        return _result;
        }

    /**
     * Note that the step configuration is only available live during the execution.
     * The StepConfiguration is not serialized, so when working with deserialized
     * events, you must use the StepId to identify the step.
     */
    @JsonIgnore
    public StepConfiguration getConfig()
	    {
	    return _config;
	    }

    private String _step_description;
    protected StepExecutionResult _result;
    private Long _stepid;
    private StepConfiguration _config;

    public static class StartStepEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "Start Step";
		    }

	    public final static String TYPE_ID = "start-step";
	    }
    public final static EventType START_INSTANCE = new StepEvent.StartStepEventType();

    public static class EndStepEventType extends EventType
	    {
	    @Override
	    public String getTypeId()
		    {
		    return TYPE_ID;
		    }

	    @Override
	    public String getName()
		    {
		    return "End Step";
		    }

	    public final static String TYPE_ID = "end-step";
	    }
    public final static EventType END_INSTANCE = new StepEvent.EndStepEventType();
    }
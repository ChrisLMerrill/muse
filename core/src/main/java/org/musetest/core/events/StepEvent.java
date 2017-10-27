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
    public StepEvent(MuseEventType type, StepConfiguration config, StepExecutionContext context)
        {
        this(type, config, context, null);
        }

    public StepEvent(MuseEventType type, StepConfiguration config, StepExecutionContext context, StepExecutionResult result)
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
        if (getType().equals(MuseEventType.StartStep))
            builder.append("Starting:");
        else if (getType().equals(MuseEventType.EndStep))
            builder.append("Finished:");
        else
            builder.append(getType().name());

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
    private StepExecutionResult _result;
    private Long _stepid;
    private StepConfiguration _config;
    }
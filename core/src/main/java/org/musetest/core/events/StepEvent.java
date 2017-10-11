package org.musetest.core.events;

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
        super(type);
        _config = config;
        _step_description = context.getProject().getStepDescriptors().get(config).getShortDescription(config);
        }

    public StepEvent(MuseEventType type, StepConfiguration config, StepExecutionContext context, StepExecutionResult result)
        {
        super(type);
        _config = config;
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

    public StepConfiguration getConfig()
        {
        return _config;
        }

    public StepExecutionResult getResult()
        {
        return _result;
        }

    private String _step_description;
    private StepExecutionResult _result;
    private StepConfiguration _config;
    }
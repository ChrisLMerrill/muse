package org.museautomation.core.steptask;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.step.*;
import org.museautomation.core.task.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("steppedtask")
public class SteppedTask extends BaseMuseTask implements ContainsStep
    {
    @SuppressWarnings("unused")   // required for Jackson serialization
    public SteppedTask()
        {
        }

    public SteppedTask(StepConfiguration step)
        {
        _step = step;
        }

    public SteppedTask(SteppedTest test)
        {
        _step = test.getStep();
        _default_variables = test.getDefaultVariables();
        setId(test.getId());
        setMetadata(test.getMetadata());
        setTags(test.getTags());
        }

    @Override
    protected boolean executeImplementation(TaskExecutionContext context)
        {
        if (!(context instanceof SteppedTaskExecutionContext))
            throw new IllegalArgumentException("SteppedTask.executeImplementation() must be provided a SteppedTaskExecutionContext. Instead, received a " + context.getClass().getSimpleName());
        SteppedTaskExecutor executor = new SteppedTaskExecutor(this, (SteppedTaskExecutionContext) context);
        context.addEventListener(new TerminateOnError(executor));
        return executor.executeAll();
        }

    @Override
    public String getDescription()
        {
        Object description = _step.getMetadataField(StepConfiguration.META_DESCRIPTION);
        if (description != null)
            return description.toString();
        else
            return super.getDescription();
        }

    public StepConfiguration getStep()
        {
        return _step;
        }

    public void setStep(StepConfiguration step)
        {
        _step = step;
        }

    @SuppressWarnings("WeakerAccess")  // used by the UI
    public Map<String, ValueSourceConfiguration> getDefaultVariables()
        {
        return _default_variables;
        }

    public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
        {
        _default_variables = default_variables;
        }

    public void setDefaultVariable(String name, ValueSourceConfiguration source)
        {
        if (_default_variables == null)
            _default_variables = new HashMap<>();
        _default_variables.put(name, source);
        }

    @Override
    public boolean equals(Object obj)
        {
        return (obj instanceof SteppedTask && Objects.equals(_step, ((SteppedTask)obj)._step));
        }

    @Override
    public String toString()
        {
        return getId();
        }

    private StepConfiguration _step;
    private Map<String, ValueSourceConfiguration> _default_variables;
    }
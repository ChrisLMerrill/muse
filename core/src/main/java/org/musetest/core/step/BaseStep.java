package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * Abstract base step implementation with some convenience methods for using value sources.
 * Also implements execute() to raise start and end events. Subclasses should override executeStep()
 * instead of execute() unless they take responsibility for raising those events.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseStep implements MuseStep
    {
    public BaseStep(StepConfiguration configuration)
        {
        _config = configuration;
        }

    @Override
    public StepConfiguration getConfiguration()
        {
        return _config;
        }

    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError
        {
        if (!_started)
            {
            _started = true;
            context.raiseEvent(new StepEvent(MuseEventType.StartStep, _config, context));
            }
        StepExecutionResult result = executeImplementation(context);
        context.raiseEvent(new StepEvent(MuseEventType.EndStep, _config, context, result));
        return result;
        }

    /**
     * Called by BaseStep.execute(). Subclasses should override this instead of execute()... or else take
     * responsibility for raising the start/end step events
     *
     * @param context The context in which the step is executed
     * @throws StepExecutionError If the step cannot be executed.
     */
    protected abstract StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError;

    /**
     * A convenience method to get a specific value source from the configuration parameter list
     * @see BaseValueSource#getValueSource
     */
    protected MuseValueSource getValueSource(StepConfiguration config, String name, boolean required, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        return BaseValueSource.getValueSource(config, name, required, project);
        }

    /**
     * A convenience method to resolves a value source to the desired type
     * @see BaseValueSource#getValueSource
     */
    protected <T> T getValue(MuseValueSource source, StepExecutionContext context, boolean null_allowed, Class<T> type) throws ValueSourceResolutionError
        {
        return BaseValueSource.getValue(source, context, null_allowed, type);
        }

    private StepConfiguration _config;
    private boolean _started = false;
    }



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
    public StepExecutionResult execute(StepExecutionContext context) throws StepExecutionError
        {
        if (!_started)
            {
            _started = true;
            context.getTestExecutionContext().raiseEvent(new StepEvent(MuseEventType.StartStep, _config, context));
            }
        StepExecutionResult result = executeImplementation(context);
        context.getTestExecutionContext().raiseEvent(new StepEvent(MuseEventType.EndStep, _config, context, result));
        return result;
        }

    /**
     * Called by BaseStep.execute(). Subclasses should override this instead of execute()... or else take
     * responsibility for raising the start/end step events
     *
     * @param context The context in which the step is executed
     * @throws StepExecutionError If the step cannot be executed.
     */
    protected abstract StepExecutionResult executeImplementation(StepExecutionContext context) throws StepExecutionError;

    /**
     * A convenience method to get a specific value source from the configuration parameter list
     *
     * @param config The step configuration from which to get the value source
     * @param name The name of the value config to get
     * @param required True if this value source is required for correct configuration
     * @param project The project the step is being instantiated in
     *
     * @return Return a configured instance, ready to execute
     *
     * @throws RequiredParameterMissingError if required=true and the parameter is not present
     * @throws MuseInstantiationException if sub-sources cannot be instantiated
     */
    protected MuseValueSource getValueSource(StepConfiguration config, String name, boolean required, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        ValueSourceConfiguration source_config = config.getSourceConfiguration(name);
        if (source_config == null)
            {
            if (required)
                throw new RequiredParameterMissingError(name);
            else
                return null;
            }
        return source_config.createSource(project);
        }

    /**
     * A convenience method to resolves a value source to the desired type
     *
     * @param source The value source to be resolved
     * @param context The context the step is executed in
     * @param null_allowed True if null is allowed
     * @param type The type that the source should resolve to
     * @param <T> The type of object the source should provide
     *
     * @return A value of the supplied type, resolved by the provided source and not null (if null_allowed = false).
     *
     * @throws NullNotAllowedError if the source resolves to null and null_allowed=false
     * @throws WrongTypeError if the source resolves to an incompatible type
     * @throws ValueSourceResolutionError if the source is null (this is likely caused by an implementation defect)
     */
    @SuppressWarnings("unchecked")
    protected <T> T getValue(MuseValueSource source, StepExecutionContext context, boolean null_allowed, Class<T> type) throws StepConfigurationError
        {
        if (source == null)
            {
            if (null_allowed)
                return null;
            throw new ValueSourceResolutionError("The source is null, but a null value from the source is not allowed. The coder of this step implementation should have detected this condition in the step constructor.");
            }
        Object value = source.resolveValue(context);
        if (value == null)
            {
            if (null_allowed)
                return null;
            throw new NullNotAllowedError(source);
            }
        if (type.isAssignableFrom(value.getClass()))
            return (T) value;
        if (type.equals(String.class))
            return (T) value.toString();
        throw new WrongTypeError(source, value);
        }

    private StepConfiguration _config;
    private boolean _started = false;
    }



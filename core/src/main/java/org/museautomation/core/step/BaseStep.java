package org.museautomation.core.step;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.strings.*;

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

    /**
     * Allows for future functionality to be performed before/after the actual implementation of the step.
     */
    @Override
    public StepExecutionResult execute(StepExecutionContext context) throws MuseExecutionError
        {
        return executeImplementation(context);
        }

    /**
     * Called by BaseStep.execute(). Subclasses should override this instead of execute().
     *
     * @param context The context in which the step is executed
     * @throws StepExecutionError If the step cannot be executed.
     */
    public abstract StepExecutionResult executeImplementation(StepExecutionContext context) throws MuseExecutionError;

    /**
     * A convenience method to get a specific value source from the configuration parameter list.
     * @see BaseValueSource#getValueSource
     */
    public MuseValueSource getValueSource(StepConfiguration config, String name, boolean required, MuseProject project) throws MuseInstantiationException
        {
        return BaseValueSource.getValueSource(config, name, required, project);
        }

    /**
     * A convenience method to resolves a value source to the desired type.
     * @see BaseValueSource#getValueSource
     */
    protected <T> T getValue(MuseValueSource source, StepExecutionContext context, boolean null_allowed, Class<T> type) throws ValueSourceResolutionError
        {
        return BaseValueSource.getValue(source, context, null_allowed, type);
        }

    /**
     * A convenience method to resolve a value source to the desired type with a default value.
     * @see BaseValueSource#getValueSource
     */
    public <T> T getValue(MuseValueSource source, StepExecutionContext context, Class<T> type, T default_value) throws ValueSourceResolutionError
        {
        T result = BaseValueSource.getValue(source, context, true, type);
        if (result == null)
            return default_value;
        return result;
        }

    /**
     * Convenience method to get a variable from the context as a specific type. Null return is not allowed.
     */
    protected <T> T getVariable(StepExecutionContext context, String varname, Class<T> type) throws MuseExecutionError
        {
        return getVariable(context, varname, type, false);
        }

    /**
     * Convenience method to get a variable from the context as a specific type. Null return may be allowed.
     */
    protected <T> T getVariable(StepExecutionContext context, String varname, Class<T> type, boolean null_allowed) throws MuseExecutionError
        {
        Object value = context.getVariable(varname);
        if (value == null)
            {
            if (null_allowed)
                return null;
            throw new MuseExecutionError(String.format("Unable to get variable '%s' from the context. A %s is required.", varname, type.getSimpleName()));
            }
        if (type.isAssignableFrom(value.getClass()))
            return (T) value;
        throw new MuseExecutionError(String.format("Variable '%s' in the context is a %s, but a %s is required.", varname, value.getClass().getSimpleName(), type.getSimpleName()));
        }

    /**
     * Convenience method to get a variable from the context as a specific type. Default value may be supplied.
     */
    protected <T> T getVariable(StepExecutionContext context, String varname, Class<T> type, T default_value) throws MuseExecutionError
        {
        Object value = context.getVariable(varname);
        if (value == null)
            return default_value;
        if (type.isAssignableFrom(value.getClass()))
            return (T) value;
        throw new MuseExecutionError(String.format("Variable '%s' in the context is a %s, but a %s is required.", varname, value.getClass().getSimpleName(), type.getSimpleName()));
        }

    @SuppressWarnings("unused") // public API
    protected String describeSource(String name, StepExecutionContext context)
        {
        ValueSourceConfiguration source_config = _config.getSources().get(name);
        return context.getProject().getValueSourceDescriptors().get(source_config).getInstanceDescription(source_config, new RootStringExpressionContext(context.getProject()));
        }

    @SuppressWarnings("unused") // public API
    public String describe(StepExecutionContext context)
        {
        return context.getProject().getStepDescriptors().get(_config).getShortDescription(_config);
        }

    private StepConfiguration _config;
    }
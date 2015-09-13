package org.musetest.core.step;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;

/**
 * Abstract base step implementation with some convenience methods for using value sources.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseStep implements MuseStep
    {
    public BaseStep(StepConfiguration configuration)
        {
        }

    @Override
    public StepConfiguration getConfiguration()
        {
        return null;
        }

    /**
     * Get a specific value source from the configuration parameter list
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
     * Resolves a value source to the desired type
     *
     * @throws NullNotAllowedError if the source resolves to null and null_allowed=false
     * @throws WrongTypeError if the source resolves to an incompatible type
     * @throws ValueSourceResolutionError if the source is null (this is likely caused by an implementation defect)
     */
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
        throw new WrongTypeError(source, value);
        }
    }



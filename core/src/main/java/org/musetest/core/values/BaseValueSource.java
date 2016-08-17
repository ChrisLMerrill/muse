package org.musetest.core.values;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseValueSource implements MuseValueSource
    {
    public BaseValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        _config = config;
        _project = project;
        }

    public ValueSourceConfiguration getConfig()
        {
        return _config;
        }

    public MuseProject getProject()
        {
        return _project;
        }

    @Override
    public String getDescription()
        {
        return _project.getValueSourceDescriptors().get(_config.getType()).getInstanceDescription(_config);
        }

    /**
     * A convenience method to get create a value source from the named source configuration
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
    public static MuseValueSource getValueSource(ContainsNamedSources config, String name, boolean required, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        ValueSourceConfiguration source_config = config.getSource(name);
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
     * A convenience method to get create a value source from the single sub-source configuration
     *
     * @param config The step configuration from which to get the value source
     * @param required True if this value source is required for correct configuration
     * @param project The project the step is being instantiated in
     *
     * @return Return a configured instance, ready to execute
     *
     * @throws RequiredParameterMissingError if required=true and the parameter is not present
     * @throws MuseInstantiationException if sub-sources cannot be instantiated
     */
    public static MuseValueSource getValueSource(ValueSourceConfiguration config, boolean required, MuseProject project) throws RequiredParameterMissingError, MuseInstantiationException
        {
        ValueSourceConfiguration source_config = config.getSource();
        if (source_config == null)
            {
            if (required)
                throw new RequiredParameterMissingError("(single sub-source)");
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
    public static <T> T getValue(MuseValueSource source, MuseExecutionContext context, boolean null_allowed, Class<T> type) throws ValueSourceResolutionError
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

    public static <T> T getValue(MuseValueSource source, MuseExecutionContext context, boolean null_allowed) throws ValueSourceResolutionError
        {
        return getValue(source, context, null_allowed, (Class<T>) Object.class);
        }

    private final ValueSourceConfiguration _config;
    private final MuseProject _project;
    }



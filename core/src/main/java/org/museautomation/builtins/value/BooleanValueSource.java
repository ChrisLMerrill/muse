package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("boolean")
@MuseValueSourceName("Boolean")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("true or false")
@MuseValueSourceLongDescription("A primitive value source that returns true or false.")
@MuseStringExpressionSupportImplementation(BooleanValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Value", description = "true or false", type = SubsourceDescriptor.Type.Value)
public class BooleanValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public BooleanValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        Object value = config.getValue();
        if (value == null)
            throw new MuseInstantiationException("Missing required value parameter");

        if (!(value instanceof Boolean))
            {
            try
                {
                Boolean boolean_value = Boolean.parseBoolean(value.toString());
                _value = boolean_value;
                LOG.warn(String.format("Non-boolean value provided for BooleanValueSource (will convert). Object of type %s was provided, converted to %s", value.getClass().getSimpleName(), boolean_value.toString()));
                return;
                }
            catch (NumberFormatException e)
                {
                LOG.warn(String.format("Non-boolean value provided for BooleanValueSource (unable to convert). Object of type %s was provided, string value = %s", value.getClass().getSimpleName(), value.toString()));
                return;
                }
            }
        _value = ((Boolean)value);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context)
        {
        return _value;
        }

    public Boolean getValue()
        {
        return _value;
        }

    private Boolean _value;

    private final static Logger LOG = LoggerFactory.getLogger(BooleanValueSource.class);

    public final static String TYPE_ID = BooleanValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
            {
            Boolean value = null;
            if (string.toLowerCase().equals("true"))
                value = true;
            else if (string.toLowerCase().equals("false"))
                value = false;

            if (value == null)
                return null;

            ValueSourceConfiguration config = new ValueSourceConfiguration();
            config.setType(BooleanValueSource.TYPE_ID);
            config.setValue(value);
            return config;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(BooleanValueSource.TYPE_ID))
                {
                if (config.getValue() != null)
                    return config.getValue().toString();
                return "???";
                }
            return null;
            }
        }
    }
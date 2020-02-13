package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("integer")
@MuseValueSourceName("Integer")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("an integer value")
@MuseValueSourceLongDescription("A primitive value source that returns an integer value")
@MuseStringExpressionSupportImplementation(IntegerValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Value", description = "an integer number", type = SubsourceDescriptor.Type.Value)
public class IntegerValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public IntegerValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        Object value = config.getValue();
        if (value == null)
            throw new MuseInstantiationException("Missing required value parameter");

        if (!(value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long))
            {
            try
                {
                Long long_value = Long.parseLong(value.toString());
                _value = long_value;
                LOG.warn(String.format("Non-integer value provided for IntegerValueSource (will convert). Object of type %s was provided, converted to %d", value.getClass().getSimpleName(), long_value));
                return;
                }
            catch (NumberFormatException e)
                {
                LOG.warn(String.format("Non-integer value provided for IntegerValueSource (unable to convert). Object of type %s was provided, string value = %s", value.getClass().getSimpleName(), value.toString()));
                return;
                }
            }
        _value = ((Number)value).longValue();
        }

    @Override
    public Object resolveValue(MuseExecutionContext context)
        {
        return _value;
        }

    public Long getValue()
        {
        return _value;
        }

    private Long  _value;

    private final static Logger LOG = LoggerFactory.getLogger(IntegerValueSource.class);

    public final static String TYPE_ID = IntegerValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
            {
            try
                {
                Long value = Long.parseLong(string);
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(IntegerValueSource.TYPE_ID);
                config.setValue(value);
                return config;
                }
            catch (NumberFormatException e)
                {
                return null;
                }
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(IntegerValueSource.TYPE_ID))
                {
                if (config.getValue() != null)
                    return config.getValue().toString();
                return "???";
                }
            return null;
            }
        }
    }
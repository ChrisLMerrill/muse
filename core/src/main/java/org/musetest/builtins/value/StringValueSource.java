package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("string")
@MuseValueSourceName("String")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("a string of characters")
@MuseValueSourceLongDescription("A primitive value source that returns string of characters")
@MuseStringExpressionSupportImplementation(StringValueSource.StringExpressionSupport.class)
@MuseSubsourceDescriptor(displayName = "Text", description = "text of the string, surrounded by quotes", type = SubsourceDescriptor.Type.Value)
public class StringValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public StringValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        Object value = config.getValue();
        if (value == null)
            throw new MuseInstantiationException("Missing required value parameter");

        if (!(value instanceof String))
            LOG.warn(String.format("Non-string value provided for StringValueSource (will convert). Object of type %s was provided, string conversion = %s", value.getClass().getSimpleName(), value.toString()));
        _value = value.toString();
        }

    public String getValue()
        {
        return _value;
        }

    public void setValue(String value)
        {
        _value = value;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context)
        {
        return _value;
        }

    @Override
    public String toString()
        {
        return _value;
        }

    private String _value;

    private final static Logger LOG = LoggerFactory.getLogger(StringValueSource.class);

    public final static String TYPE_ID = StringValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
            {
            if (string.length() > 1 && string.startsWith("\"") && string.endsWith("\""))
                {
                ValueSourceConfiguration config = new ValueSourceConfiguration();
                config.setType(StringValueSource.TYPE_ID);
                config.setValue(string.substring(1, string.length() - 1));
                return config;
                }
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(StringValueSource.TYPE_ID))
                {
                if (config.getValue() != null)
                    return "\"" + config.getValue() + "\"";
                return "???";
                }
            return null;
            }
        }
    }
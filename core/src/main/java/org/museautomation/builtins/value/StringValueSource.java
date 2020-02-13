package org.museautomation.builtins.value;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
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
        _value_resolved = null;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context)
        {
        if (_value_resolved == null)
            {
            if (_value.contains("\\"))
                {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < _value.length(); i++)
                    {
                    char next = _value.charAt(i);
                    if (next == '\\')
                        {
                        i++;
                        char escaped = _value.charAt(i);
                        switch (escaped)
                            {
                            case '\\':
                                builder.append('\\');
                                break;
                            case '\"':
                                builder.append('\"');
                                break;
                            case '\'':
                                builder.append('\'');
                                break;
                            case 'n':
                                builder.append('\n');
                                break;
                            case 't':
                                builder.append('\t');
                                break;
                            default:
                                builder.append('\\');
                                builder.append(escaped);
                            }
                        }
                    else
                        builder.append(next);
                    }
                _value_resolved = builder.toString();
                }
            else
                _value_resolved = _value;
            }
        return _value_resolved;
        }

    @Override
    public String toString()
        {
        return _value;
        }

    private String _value;
    private transient String _value_resolved = null;

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
                String parsed = string.substring(1, string.length() - 1);
                config.setValue(unescape(parsed));
                return config;
                }
            return null;
            }

        private String unescape(String parsed)
            {
            return parsed.replaceAll("\\\\\"", "\"");
            }

        @Override
        public String toString(ValueSourceConfiguration config, StringExpressionContext context, int depth)
            {
            if (config.getType().equals(StringValueSource.TYPE_ID))
                {
                String value = config.getValue().toString();
                if (value != null)
                    {
                    value = value.replaceAll("\"", "\\\"");
                    return "\"" + value + "\"";
                    }
                return "???";
                }
            return null;
            }
        }
    }
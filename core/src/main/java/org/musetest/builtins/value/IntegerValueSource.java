package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.slf4j.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("integer")
@MuseValueSourceName("Integer")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("an integer value")
@MuseValueSourceLongDescription("A primitive value source that returns an integer value")
@MuseStringExpressionSupportImplementation(IntegerValueSourceStringExpressionSupport.class)
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
    public Object resolveValue(StepExecutionContext context)
        {
        return _value;
        }

    public Long getValue()
        {
        return _value;
        }

    private Long  _value;

    final static Logger LOG = LoggerFactory.getLogger(IntegerValueSource.class);

    public final static String TYPE_ID = IntegerValueSource.class.getAnnotation(MuseTypeId.class).value();
    }
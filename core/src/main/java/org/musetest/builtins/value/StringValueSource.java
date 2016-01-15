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
@MuseTypeId("string")
@MuseValueSourceName("String")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("a string of characters")
@MuseValueSourceLongDescription("A primitive value source that returns string of characters")
@MuseStringExpressionSupportImplementation(StringValueSourceStringExpressionSupport.class)
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
    public Object resolveValue(StepExecutionContext context)
        {
        return _value;
        }

    @Override
    public String toString()
        {
        return _value;
        }

    private String _value;

    final static Logger LOG = LoggerFactory.getLogger(StringValueSource.class);

    public final static String TYPE_ID = StringValueSource.class.getAnnotation(MuseTypeId.class).value();
    }
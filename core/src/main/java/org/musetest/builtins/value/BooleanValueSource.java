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
@MuseTypeId("boolean")
@MuseValueSourceName("Boolean")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("true or false")
@MuseValueSourceLongDescription("A primitive value source that returns true or false.")
@MuseStringExpressionSupportImplementation(BooleanValueSource.class)
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
    public Boolean resolveValue(StepExecutionContext context)
        {
        return _value;
        }

    public Boolean getValue()
        {
        return _value;
        }

    private Boolean _value;

    final static Logger LOG = LoggerFactory.getLogger(BooleanValueSource.class);

    public final static String TYPE_ID = BooleanValueSource.class.getAnnotation(MuseTypeId.class).value();
    }
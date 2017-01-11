package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("null")
@MuseValueSourceName("Null")
@MuseValueSourceTypeGroup("Primitives")
@MuseValueSourceShortDescription("a null reference")
@MuseValueSourceLongDescription("A primitive value source that returns a null reference")
@MuseStringExpressionSupportImplementation(NullValueSource.StringExpressionSupport.class)
public class NullValueSource extends BaseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public NullValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        super(config, project);
        }

    @Override
    public Boolean resolveValue(MuseExecutionContext context)
        {
        return null;
        }

    public final static String TYPE_ID = NullValueSource.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // needs public static access to be discovered and instantiated via reflection
    public static class StringExpressionSupport extends BaseValueSourceStringExpressionSupport
        {
        @Override
        public ValueSourceConfiguration fromLiteral(String string, MuseProject project)
            {
            if (string.equals(NULL))
                return ValueSourceConfiguration.forType(NULL);
            return null;
            }

        @Override
        public String toString(ValueSourceConfiguration config, MuseProject project, int depth)
            {
            if (NullValueSource.TYPE_ID.equals(config.getType()))
                return NULL;
            return null;
            }

        public final static String NULL = "null";
        }
    }
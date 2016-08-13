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
@MuseStringExpressionSupportImplementation(NullValueSourceStringExpressionSupport.class)
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
    }
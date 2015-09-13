package org.musetest.builtins.value;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("null")
@MuseValueSourceName("null")
@MuseValueSourceDescription("null")
@MuseValueSourceTypeGroup("Primitives")
public class NullValueSource implements MuseValueSource
    {
    @SuppressWarnings("unused")  // used via reflection
    public NullValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        }

    @Override
    public Boolean resolveValue(StepExecutionContext context)
        {
        return null;
        }

    @Override
    public String getDescription()
        {
        return "null";
        }

    public final static String TYPE_ID = NullValueSource.class.getAnnotation(MuseTypeId.class).value();
    }
package org.museautomation.core.resource.json;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.*;
import com.fasterxml.jackson.databind.jsontype.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class IntegerAsLongDeserializer extends UntypedObjectDeserializer
    {
    @Override
    public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
        {
        Object out = super.deserialize(jp, ctxt);
        if (out instanceof Integer)
            return Long.valueOf((Integer) out);
        return out;
        }

    @Override
    public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException
        {
        Object out = super.deserializeWithType(jp, ctxt, typeDeserializer);
        if (out instanceof Integer)
            return Long.valueOf((Integer) out);
        return out;
        }
    }

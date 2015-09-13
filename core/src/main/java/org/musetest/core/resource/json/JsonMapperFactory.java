package org.musetest.core.resource.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.module.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JsonMapperFactory
    {
    public static ObjectMapper createMapper(TypeLocator type_locator)
        {
        if (MAPPER.get() == null)
            {
            ObjectMapper mapper = new ObjectMapper();

            for (String type_id : type_locator.getTypes())
                mapper.registerSubtypes(new NamedType(type_locator.getClassForTypeId(type_id), type_id));

            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            SimpleModule mod = new SimpleModule().addDeserializer(Object.class, new IntegerAsLongDeserializer());
            mapper.registerModule(mod);

            MAPPER.set(mapper);
            }

        return MAPPER.get();
        }

    private static ThreadLocal<ObjectMapper> MAPPER = new ThreadLocal<>();
    }



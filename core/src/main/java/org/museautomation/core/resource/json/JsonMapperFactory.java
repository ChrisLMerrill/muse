package org.museautomation.core.resource.json;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.module.*;
import org.museautomation.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JsonMapperFactory
    {
    public static ObjectMapper getDefaultMapper()
        {
        if (DEFAULT_MAPPER == null)
            DEFAULT_MAPPER = createDefaultMapper();
        return DEFAULT_MAPPER;
        }

    public static ObjectMapper createDefaultMapper()
        {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        return mapper;
        }

    public static ObjectMapper createMapper(TypeLocator type_locator)
        {
        if (TYPED_MAPPER.get() == null)
            {
            ObjectMapper mapper = createDefaultMapper();

            for (String type_id : type_locator.getTypes())
                mapper.registerSubtypes(new NamedType(type_locator.getClassForTypeId(type_id), type_id));

            SimpleModule mod = new SimpleModule().addDeserializer(Object.class, new IntegerAsLongDeserializer());
            mapper.registerModule(mod);

            TYPED_MAPPER.set(mapper);
            }

        return TYPED_MAPPER.get();
        }

    private static ObjectMapper DEFAULT_MAPPER = null;
    private static ThreadLocal<ObjectMapper> TYPED_MAPPER = new ThreadLocal<>();
    }



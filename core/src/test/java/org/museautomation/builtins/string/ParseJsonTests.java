package org.museautomation.builtins.string;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.string.*;
import org.museautomation.core.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ParseJsonTests
    {
    @SuppressWarnings("rawtypes")
    @Test
    public void testJsonParsing() throws ValueSourceResolutionError, MuseInstantiationException
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forTypeWithSource(ParseJsonValueSource.TYPE_ID, "{\"list\":[123,456]}");
        MuseValueSource source = config.createSource();
        Object obj = source.resolveValue(new MockStepExecutionContext());
        assertTrue(obj instanceof Map);
        obj = ((Map)obj).get("list");
        assertTrue(obj instanceof List);
        assertEquals(123, ((List)obj).get(0));
        }
    }
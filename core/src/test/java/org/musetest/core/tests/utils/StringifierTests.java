package org.musetest.core.tests.utils;

import org.junit.jupiter.api.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class StringifierTests
    {
    @Test
    void stringifier()
        {
        Assertions.assertEquals(MockStringifier.OUTPUT, Stringifiers.find(new MockStringifier()).toString());

        String unknown_thing = "this thing";
        Assertions.assertEquals(unknown_thing, Stringifiers.find(unknown_thing).toString());
        }
    }



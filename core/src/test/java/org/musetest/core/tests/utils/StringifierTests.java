package org.musetest.core.tests.utils;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringifierTests
    {
    @Test
    public void stringifier()
        {
        Assertions.assertEquals(MockStringifier.OUTPUT, Stringifiers.find(new MockStringifier()).toString());

        String unknown_thing = "this thing";
        Assertions.assertEquals(unknown_thing, Stringifiers.find(unknown_thing).toString());
        }
    }



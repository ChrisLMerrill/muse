package org.musetest.core.tests.utils;

import org.junit.*;
import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringifierTests
    {
    @Test
    public void stringifier()
        {
        Assert.assertEquals(MockStringifier.OUTPUT, Stringifiers.find(new MockStringifier()).toString());

        String unknown_thing = "this thing";
        Assert.assertEquals(unknown_thing, Stringifiers.find(unknown_thing).toString());
        }
    }



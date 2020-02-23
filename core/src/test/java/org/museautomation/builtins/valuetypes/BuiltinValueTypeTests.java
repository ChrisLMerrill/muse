package org.museautomation.builtins.valuetypes;

import org.junit.jupiter.api.*;
import org.museautomation.core.project.*;
import org.museautomation.core.valuetypes.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BuiltinValueTypeTests
    {
    @Test
    void stringType()
        {
        MuseValueTypes types = new SimpleProject().getValueTypes();
        StringValueType type = new StringValueType();
        Assertions.assertTrue(types.getValueTypes().contains(type));
        Assertions.assertTrue(type.isInstance("abc"));
        }

    @Test
    void integerType()
        {
        MuseValueTypes types = new SimpleProject().getValueTypes();
        IntegerValueType type = new IntegerValueType();
        Assertions.assertTrue(types.getValueTypes().contains(type));
        Assertions.assertTrue(type.isInstance(123L));
        Assertions.assertFalse(type.isInstance(123));
        Assertions.assertFalse(type.isInstance(12.3));
        }

    @Test
    void anyType()
        {
        MuseValueTypes types = new SimpleProject().getValueTypes();
        AnyValueType type = new AnyValueType();
        Assertions.assertTrue(types.getValueTypes().contains(type));
        Assertions.assertTrue(type.isInstance(123L));
        Assertions.assertTrue(type.isInstance(123));
        Assertions.assertTrue(type.isInstance(true));
        Assertions.assertTrue(type.isInstance(null));
        Assertions.assertTrue(type.isInstance(12.3));
        Assertions.assertTrue(type.isInstance(types));
        Assertions.assertTrue(type.isInstance(Collections.emptyList()));
        Assertions.assertTrue(type.isInstance(new Random()));
        }
    }
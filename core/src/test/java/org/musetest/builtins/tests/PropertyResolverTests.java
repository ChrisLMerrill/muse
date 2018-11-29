package org.musetest.builtins.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.builtins.value.property.*;
import org.musetest.core.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PropertyResolverTests
    {
    @Test
    public void reflectionResolverByMethodName() throws MuseExecutionError
        {
        // calls the "length" method on String
        final Object target = "1234567";
        final String name = "length";
        final Object result = ((String)target).length();

        PropertyResolver resolver = new MethodByReflectionResolver();
        Assertions.assertTrue(resolver.canResolve(target, name));
        Assertions.assertEquals(result, resolver.resolve(target, name));
        }

    @Test
    public void reflectionResolverByBeanGetter() throws MuseExecutionError
        {
        // calls getClass() method on an object
        final Object target = "blah";
        final String name = "class";
        final Object result = target.getClass();

        PropertyResolver resolver = new MethodByReflectionResolver();
        Assertions.assertTrue(resolver.canResolve(target, name));
        Assertions.assertEquals(result, resolver.resolve(target, name));
        }

    @Test
    public void reflectionResolverByField() throws MuseExecutionError
        {
        class ObjectWithField
            {
            @SuppressWarnings("WeakerAccess")
            public Object field1;
            }
        final String name = "field1";
        final Object result = UUID.randomUUID();

        ObjectWithField target = new ObjectWithField();
        target.field1 = result;

        PropertyResolver resolver = new FieldByReflectionResolver();
        Assertions.assertTrue(resolver.canResolve(target, name));
        Assertions.assertEquals(result, resolver.resolve(target, name));
        }

    @Test
    public void mapResolver() throws MuseExecutionError
        {
        Map<String, Object> map = new HashMap<>();
        final String name = "thename";
        final UUID result = UUID.randomUUID();
        map.put(name, result);

        PropertyResolver resolver = new MapResolver();
        Assertions.assertTrue(resolver.canResolve(map, name));
        Assertions.assertEquals(result, resolver.resolve(map, name));
        Assertions.assertNull(resolver.resolve(map, "not-exists"));
        }
    }



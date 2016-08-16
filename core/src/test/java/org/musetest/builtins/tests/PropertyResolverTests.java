package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.value.property.*;
import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class PropertyResolverTests
    {
    @Test
    public void reflectionResolver() throws MuseExecutionError
        {
        // calls the "length" method on String
        final Object target = "1234567";
        final String name = "length";
        final Object result = 7;

        PropertyResolver resolver = new ReflectionPropertyResolver();
        Assert.assertTrue(resolver.canResolve(target, name));
        Assert.assertEquals(result, resolver.resolve(target, name));
        }
    }



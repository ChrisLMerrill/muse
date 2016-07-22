package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.tests.mocks.*;
import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ClassScanningTests
    {
    @Test
    public void testReflectionsClassScanning()
        {
        try
            {
            Class.forName("org.reflections.Reflections");
            }
        catch (ClassNotFoundException e)
            {
            LoggerFactory.getLogger(this.getClass()).error("Can't find Reflections lib", e);
            }
        Reflections reflections = new Reflections("org.musetest.core.tests.mocks");
        Set<Class<? extends MuseResourceFactory>> factories = reflections.getSubTypesOf(MuseResourceFactory.class);

        Assert.assertTrue("Did not find the MockResourceFactory", factories.contains(MockResourceFactory.class));
        }
    }



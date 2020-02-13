package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.tests.mocks.*;
import org.reflections.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ClassScanningTests
    {
    @Test
    void testReflectionsClassScanning()
        {
        try
            {
            Class.forName("org.reflections.Reflections");
            }
        catch (ClassNotFoundException e)
            {
            LoggerFactory.getLogger(this.getClass()).error("Can't find Reflections lib", e);
            }
        Reflections reflections = new Reflections("org.museautomation.core.tests.mocks");
        Set<Class<? extends MuseResourceFactory>> factories = reflections.getSubTypesOf(MuseResourceFactory.class);

        Assertions.assertTrue(factories.contains(MockResourceFactory.class), "Did not find the MockResourceFactory");
        }
    }



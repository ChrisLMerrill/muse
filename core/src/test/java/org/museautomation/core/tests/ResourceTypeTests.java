package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.suite.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ResourceTypeTests
    {
    @Test
    void findPrimaryTypes() // not Subtypes
        {
        MuseProject project = new SimpleProject();
        Collection<ResourceType> all = project.getResourceTypes().getPrimary();

        Assertions.assertTrue(all.contains(new MuseTest.TestResourceType()));
        Assertions.assertTrue(all.contains(new PluginConfiguration.PluginConfigurationResourceType()));
        // subtype should not be found
        Assertions.assertFalse(all.contains(new IdListTestSuite.IdListTestSuiteSubtype()));
        }

    @Test
    void findSubtypeOf()
        {
        MuseProject project = new SimpleProject();
        Collection<ResourceSubtype> found = project.getResourceTypes().getSubtypesOf(new MuseTestSuite.TestSuiteResourceType());
        Assertions.assertEquals(2, found.size());
        Assertions.assertTrue(found.contains(new ParameterListTestSuite.ParameterListTestSuiteSubtype()));
        Assertions.assertTrue(found.contains(new IdListTestSuite.IdListTestSuiteSubtype()));
        }
    }



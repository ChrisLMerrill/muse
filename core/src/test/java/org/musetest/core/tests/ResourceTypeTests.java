package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.suite.*;
import org.musetest.core.test.plugin.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceTypeTests
    {
    @Test
    public void findPrimaryTypes() // not Subtypes
        {
        MuseProject project = new SimpleProject();
        Collection<ResourceType> all = project.getResourceTypes().getPrimary();

        Assert.assertTrue(all.contains(new MuseTest.TestResourceType()));
        Assert.assertTrue(all.contains(new TestPluginConfiguration.TestPluginConfigurationResourceType()));
        // subtype should not be found
        Assert.assertFalse(all.contains(new IdListTestSuite.IdListTestSuiteSubtype()));
        }

    @Test
    public void findSubtypeOf()
        {
        MuseProject project = new SimpleProject();
        Collection<ResourceSubtype> found = project.getResourceTypes().getSubtypesOf(new MuseTestSuite.TestSuiteResourceType());
        Assert.assertEquals(2, found.size());
        Assert.assertTrue(found.contains(new ParameterListTestSuite.ParameterListTestSuiteSubtype()));
        Assert.assertTrue(found.contains(new IdListTestSuite.IdListTestSuiteSubtype()));
        }
    }



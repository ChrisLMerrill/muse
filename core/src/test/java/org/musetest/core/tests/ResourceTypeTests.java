package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.suite.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceTypeTests
    {
    @Test
    public void findAllTypes()
        {
        MuseProject project = new SimpleProject();
        Collection<ResourceType> all = project.getResourceTypes().getAll();

        Assert.assertTrue(all.contains(new MuseTest.TestResourceType()));
        Assert.assertTrue(all.contains(new ContextInitializerConfigurations.ContextInitializerConfigurationsResourceType()));
        }

    @Test
    public void findSubtypeOf()
        {
        MuseProject project = new SimpleProject();
        Collection<ResourceSubtype> found = project.getResourceSubtypes().getSubtypesOf(new MuseTestSuite.TestSuiteResourceType());
        Assert.assertEquals(2, found.size());
        Assert.assertTrue(found.contains(new ParameterListTestSuite.ParameterListTestSuiteSubtype()));
        Assert.assertTrue(found.contains(new IdListTestSuite.IdListTestSuiteSubtype()));
        }
    }


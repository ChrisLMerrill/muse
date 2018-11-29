package org.musetest.core.tests;


import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ResourceStorageTests
    {
    @Test
    void findResourceByIdAndType() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.setId("test1");
        project.getResourceStorage().addResource(test);
        Assertions.assertNotNull(project.getResourceStorage().getResource("test1", MuseTest.class));
        }

    @Test
    void findSingleResourceById() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTest test1 = new MockTest();
        test1.setId("test1");
        project.getResourceStorage().addResource(test1);

        MuseTest test2 = new MockTest();
        test2.setId("test2");
        project.getResourceStorage().addResource(test2);

        MuseResource resource = project.getResourceStorage().getResource("test1");
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(test1, resource, "should find the right resource");

        ResourceToken<MuseTest> token = project.getResourceStorage().findResource("test2");
        Assertions.assertNotNull(token);
        Assertions.assertEquals(test2, token.getResource(), "token doesn't have the right resource");
        }

    @Test
    void findMultipleResourcesByType() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.setId("Test1");
        project.getResourceStorage().addResource(test);

        test = new MockTest();
        test.setId("Test2");
        project.getResourceStorage().addResource(test);

        List<ResourceToken> resources = project.getResourceStorage().findResources(new ResourceQueryParameters(new MuseTest.TestResourceType()));
        Assertions.assertEquals(2, resources.size(), "Should find 2 resources");
        Assertions.assertTrue(resources.get(0).getId().equals("Test1") ^ resources.get(1).getId().equals("Test1"), "Should find one resource with id 'Test1'");
        Assertions.assertTrue(resources.get(0).getId().equals("Test2") ^ resources.get(1).getId().equals("Test2"), "Should find one resource with id 'Test2'");
        }

    @Test
    void addAndRemoveResourceEvents() throws IOException
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTest();
        test.setId("Test1");

        AtomicReference<ResourceToken> resource_added = new AtomicReference(null);
        AtomicReference<ResourceToken> resource_removed = new AtomicReference(null);
        ProjectResourceListener listener = new ProjectResourceListener()
            {
            @Override
            public void resourceAdded(ResourceToken added)
                {
                resource_added.set(added);
                }

            @Override
            public void resourceRemoved(ResourceToken removed)
                {
                resource_removed.set(removed);
                }
            };
        project.addResourceListener(listener);

        project.getResourceStorage().addResource(test);
        Assertions.assertNotNull(resource_added.get());
        Assertions.assertEquals(test.getId(), resource_added.get().getId());

        project.getResourceStorage().removeResource(new InMemoryResourceToken(test));
        Assertions.assertNotNull(resource_removed.get());
        Assertions.assertEquals(test.getId(), resource_removed.get().getId());

        // ensure listener is deregistered
        resource_added.set(null);
        project.removeResourceListener(listener);
        project.getResourceStorage().addResource(test);
        Assertions.assertNull(resource_added.get());
        }

    @Test
    void refuseToAddDuplicateResource() throws IOException
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTest();
        test.setId("Test1");
        project.getResourceStorage().addResource(test);

        MuseTest duplicate = new MockTest();
        duplicate.setId("Test1");
        try
            {
            project.getResourceStorage().addResource(duplicate);
            Assertions.fail("should have thown an exception");
            }
        catch (IllegalArgumentException e)
            {
            // pass
            }
        }

    @Test
    void addInvalidFilename()
        {
        Assertions.assertFalse(new FilenameValidator().isValid("test<1>"), "bad filename would have been accepted");
        }
    }



package org.museautomation.core.tests;


import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.storage.*;

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

        MuseTask test = new MockTask();
        test.setId("test1");
        project.getResourceStorage().addResource(test);
        Assertions.assertNotNull(project.getResourceStorage().getResource("test1", MuseTask.class));
        }

    @Test
    void findSingleResourceById() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTask test1 = new MockTask();
        test1.setId("test1");
        project.getResourceStorage().addResource(test1);

        MuseTask test2 = new MockTask();
        test2.setId("test2");
        project.getResourceStorage().addResource(test2);

        MuseResource resource = project.getResourceStorage().getResource("test1");
        Assertions.assertNotNull(resource);
        Assertions.assertEquals(test1, resource, "should find the right resource");

        ResourceToken<MuseResource> token = project.getResourceStorage().findResource("test2");
        Assertions.assertNotNull(token);
        Assertions.assertEquals(test2, token.getResource(), "token doesn't have the right resource");
        }

    @Test
    void findMultipleResourcesByType() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTask test = new MockTask();
        test.setId("Test1");
        project.getResourceStorage().addResource(test);

        test = new MockTask();
        test.setId("Test2");
        project.getResourceStorage().addResource(test);

        List<ResourceToken<MuseResource>> resources = project.getResourceStorage().findResources(new ResourceQueryParameters(new MuseTask.TaskResourceType()));
        Assertions.assertEquals(2, resources.size(), "Should find 2 resources");
        Assertions.assertTrue(resources.get(0).getId().equals("Test1") ^ resources.get(1).getId().equals("Test1"), "Should find one resource with id 'Test1'");
        Assertions.assertTrue(resources.get(0).getId().equals("Test2") ^ resources.get(1).getId().equals("Test2"), "Should find one resource with id 'Test2'");
        }

    @Test
    void addAndRemoveResourceEvents() throws IOException
        {
        MuseProject project = new SimpleProject();
        MuseTask test = new MockTask();
        test.setId("Test1");

        AtomicReference<ResourceToken<MuseResource>> resource_added = new AtomicReference<>(null);
        AtomicReference<ResourceToken<MuseResource>> resource_removed = new AtomicReference<>(null);
        ProjectResourceListener listener = new ProjectResourceListener()
            {
            @Override
            public void resourceAdded(ResourceToken<MuseResource> added)
                {
                resource_added.set(added);
                }

            @Override
            public void resourceRemoved(ResourceToken<MuseResource> removed)
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
        MuseTask test = new MockTask();
        test.setId("Test1");
        project.getResourceStorage().addResource(test);

        MuseTask duplicate = new MockTask();
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
package org.musetest.core.tests;


import org.junit.*;
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
public class ResourceStorageTests
    {
    @Test
    public void findResourceByIdAndType() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.setId("test1");
        project.getResourceStorage().addResource(test);
        Assert.assertNotNull(project.getResourceStorage().getResource("test1", MuseTest.class));
        }

    @Test
    public void findSingleResourceById() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTest test1 = new MockTest();
        test1.setId("test1");
        project.getResourceStorage().addResource(test1);

        MuseTest test2 = new MockTest();
        test2.setId("test2");
        project.getResourceStorage().addResource(test2);

        MuseResource resource = project.getResourceStorage().getResource("test1");
        Assert.assertNotNull(resource);
        Assert.assertEquals("should find the right resource", test1, resource);

        ResourceToken<MuseTest> token = project.getResourceStorage().findResource("test2");
        Assert.assertNotNull(token);
        Assert.assertEquals("token doesn't have the right resource", test2, token.getResource());
        }

    @Test
    public void findMultipleResourcesByType() throws IOException
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.setId("Test1");
        project.getResourceStorage().addResource(test);

        test = new MockTest();
        test.setId("Test2");
        project.getResourceStorage().addResource(test);

        List<ResourceToken> resources = project.getResourceStorage().findResources(new ResourceQueryParameters(new MuseTest.TestResourceType()));
        Assert.assertEquals("Should find 2 resources", 2, resources.size());
        Assert.assertTrue("Should find one resource with id 'Test1'", resources.get(0).getId().equals("Test1") ^ resources.get(1).getId().equals("Test1"));
        Assert.assertTrue("Should find one resource with id 'Test2'", resources.get(0).getId().equals("Test2") ^ resources.get(1).getId().equals("Test2"));
        }

    @Test
    public void addAndRemoveResourceEvents() throws IOException
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
        Assert.assertNotNull(resource_added.get());
        Assert.assertEquals(test.getId(), resource_added.get().getId());

        project.getResourceStorage().removeResource(new InMemoryResourceToken(test));
        Assert.assertNotNull(resource_removed.get());
        Assert.assertEquals(test.getId(), resource_removed.get().getId());

        // ensure listener is deregistered
        resource_added.set(null);
        project.removeResourceListener(listener);
        project.getResourceStorage().addResource(test);
        Assert.assertNull(resource_added.get());
        }

    @Test
    public void refuseToAddDuplicateResource() throws IOException
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
            Assert.assertTrue("should have thown an exception", false);
            }
        catch (IllegalArgumentException e)
            {
            // pass
            }
        }

    @Test
    public void addInvalidFilename() throws IOException
        {
        Assert.assertFalse("bad filename would have been accepted", new FilenameValidator().isValid("test<1>"));
        }
    }



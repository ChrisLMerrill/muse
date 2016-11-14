package org.musetest.core.tests;


import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.tests.mocks.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectTests
    {
    @Test
    public void findResourceByIdAndType()
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.setId("test1");
        project.addResource(test);
        Assert.assertNotNull(project.getResource("test1", MuseTest.class));
        }

    @Test
    public void findSingleResourceById()
        {
        MuseProject project = new SimpleProject();

        MuseTest test1 = new MockTest();
        test1.setId("test1");
        project.addResource(test1);

        MuseTest test2 = new MockTest();
        test2.setId("test2");
        project.addResource(test2);

        MuseResource resource = project.getResource("test1");
        Assert.assertEquals("should find the right resource", test1, resource);
        }

    @Test
    public void findMultipleResourcesByType()
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.setId("Test1");
        project.addResource(test);

        test = new MockTest();
        test.setId("Test2");
        project.addResource(test);

        List<ResourceToken> resources = project.findResources(new ResourceAttributes(new MuseTest.TestResourceType()));
        Assert.assertEquals("Should find 2 resources", 2, resources.size());
        Assert.assertTrue("Should find one resource with id 'Test1'", resources.get(0).getId().equals("Test1") ^ resources.get(1).getId().equals("Test1"));
        Assert.assertTrue("Should find one resource with id 'Test2'", resources.get(0).getId().equals("Test2") ^ resources.get(1).getId().equals("Test2"));
        }

    @Test
    public void addAndRemoveResourceEvents()
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTest();
        test.setId("Test1");

        AtomicReference<ResourceToken> resource_added = new AtomicReference(null);
        AtomicReference<ResourceToken> resource_removed = new AtomicReference(null);
        project.addResourceListener(new ProjectResourceListener()
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
            });

        project.addResource(test);
        Assert.assertNotNull(resource_added.get());
        Assert.assertEquals(test.getId(), resource_added.get().getId());

        project.removeResource(new InMemoryResourceToken(test));
        Assert.assertNotNull(resource_removed.get());
        Assert.assertEquals(test.getId(), resource_removed.get().getId());
        }

    @Test
    public void refuseToAddDuplicateResource()
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTest();
        test.setId("Test1");
        project.addResource(test);

        MuseTest duplicate = new MockTest();
        duplicate.setId("Test1");
        try
            {
            project.addResource(duplicate);
            Assert.assertTrue("should have thown an exception", false);
            }
        catch (IllegalArgumentException e)
            {
            // pass
            }
        }
    }



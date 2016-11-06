package org.musetest.core.tests;


import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ProjectTests
    {
    @Test
    public void testResourceByIdAndType()
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.getMetadata().setId("test1");
        test.getMetadata().setType(ResourceTypes.Test);
        project.addResource(test);
        Assert.assertNotNull(project.findResource("test1", MuseTest.class));
        }

    @Test
    public void testSingleResourceById()
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.getMetadata().setId("test1");
        project.addResource(test);

        test = new MockTest();
        test.getMetadata().setId("test2");
        project.addResource(test);

        ResourceMetadata filter = new ResourceMetadata();
        filter.setId("test1");
        List<ResourceToken> resources = project.findResources(filter);
        Assert.assertEquals("Should only find one resource", 1, resources.size());
        Assert.assertEquals("Should find resource with id = test1", "test1", resources.get(0).getMetadata().getId());
        Assert.assertEquals("Should find resource with id = test1", "test1", project.findResource(filter).getMetadata().getId());
        }

    @Test
    public void testMultipleResourcesByType()
        {
        MuseProject project = new SimpleProject();

        MuseTest test = new MockTest();
        test.getMetadata().setType(ResourceTypes.Test);
        test.getMetadata().setId("Test1");
        project.addResource(test);

        test = new MockTest();
        test.getMetadata().setType(ResourceTypes.Test);
        test.getMetadata().setId("Test2");
        project.addResource(test);

        List<ResourceToken> resources = project.findResources(new ResourceMetadata(ResourceTypes.Test));
        Assert.assertEquals("Should find 2 resources", 2, resources.size());
        Assert.assertTrue("Should find one resource with id 'Test1'", resources.get(0).getMetadata().getId().equals("Test1") ^ resources.get(1).getMetadata().getId().equals("Test1"));
        Assert.assertTrue("Should find one resource with id 'Test2'", resources.get(0).getMetadata().getId().equals("Test2") ^ resources.get(1).getMetadata().getId().equals("Test2"));
        }

    @Test
    public void addAndRemoveResourceEvents()
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTest();
        test.getMetadata().setType(ResourceTypes.Test);
        test.getMetadata().setId("Test1");

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
        Assert.assertEquals(test.getMetadata().getId(), resource_added.get().getMetadata().getId());

        project.removeResource(new InMemoryResourceToken(test));
        Assert.assertNotNull(resource_removed.get());
        Assert.assertEquals(test.getMetadata().getId(), resource_removed.get().getMetadata().getId());
        }

    @Test
    public void refuseToAddDuplicateResource()
        {
        MuseProject project = new SimpleProject();
        MuseTest test = new MockTest();
        test.getMetadata().setType(ResourceTypes.Test);
        test.getMetadata().setId("Test1");
        project.addResource(test);

        MuseTest duplicate = new MockTest();
        duplicate.getMetadata().setType(ResourceTypes.Test);
        duplicate.getMetadata().setId("Test1");
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



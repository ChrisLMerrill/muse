package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.tests.utils.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceFactoryTests
    {
    /**
     * Test the dynamic scanning for ResourceFactories by pushing in a ResourceOrigin that
     * could be only satisfied by a dynamically-detected resource factory (because it isn't
     * built into Muse Core). Ensure that a resource is loaded and accessible.
     */
    @Test
    public void testResourceFactoryLoading()
        {
        InMemoryResourceStorage store = new InMemoryResourceStorage();
        MuseProject project = new SimpleProject(store);
        MockTest test = new MockTest();
        test.setId("test1");
        store.loadResource(new MockResourceOrigin(test));

        final List<ResourceToken> resources = project.getResourceStorage().findResources(new ResourceAttributes(new MuseTest.TestResourceType()));
        Assert.assertEquals(1, resources.size());
        Assert.assertEquals("test1", resources.get(0).getId());
        }

    @Test
    public void testOpenProjectWithFileResource()
        {
        FolderIntoMemoryResourceStorage store = new FolderIntoMemoryResourceStorage(TestUtils.getTestResource("projects/files", getClass()));
        MuseProject project = new SimpleProject(store);
        MuseResource resource = project.getResourceStorage().getResource("not_done_yet", MockMuseResource.class);
        Assert.assertNull(resource);
        }
    }



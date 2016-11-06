package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
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
        InMemoryResourceStore store = new InMemoryResourceStore();
        MuseProject project = new SimpleProject(store);
        MockTest test = new MockTest();
        test.getMetadata().setId("test1");
        store.loadResource(new MockResourceOrigin(test));

        final List<ResourceToken> resources = project.findResources(new ResourceMetadata(ResourceTypes.Test));
        Assert.assertEquals(1, resources.size());
        Assert.assertEquals("test1", resources.get(0).getMetadata().getId());
        }

    @Test
    public void testOpenProjectWithFileResource()
        {
        FolderIntoMemoryResourceStore store = new FolderIntoMemoryResourceStore(TestUtils.getTestResource("projects/files", getClass()));
        MuseProject project = new SimpleProject(store);
        ResourceToken resource = project.findResource("not_done_yet", MockMuseResource.class);
        Assert.assertNull(resource);
        }
    }



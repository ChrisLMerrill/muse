package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.util.*;
import org.musetest.tests.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ResourceFactoryTests
    {
    /**
     * Test the dynamic scanning for ResourceFactories by pushing in a ResourceOrigin that
     * could be only satisfied by a dynamically-detected resource factory (because it isn't
     * built into Muse Core). Ensure that a resource is loaded and accessible.
     */
    @Test
    void testResourceFactoryLoading()
        {
        InMemoryResourceStorage store = new InMemoryResourceStorage();
        MuseProject project = new SimpleProject(store);
        MockTest test = new MockTest();
        test.setId("test1");
        store.loadResource(new MockResourceOrigin(test));

        final List<ResourceToken> resources = project.getResourceStorage().findResources(new ResourceQueryParameters(new MuseTest.TestResourceType()));
        Assertions.assertEquals(1, resources.size());
        Assertions.assertEquals("test1", resources.get(0).getId());
        }

    @Test
    void testOpenProjectWithFileResource()
        {
        FolderIntoMemoryResourceStorage store = new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/empty", getClass()));
        MuseProject project = new SimpleProject(store);
        MuseResource resource = project.getResourceStorage().getResource("not_done_yet", MockMuseResource.class);
        Assertions.assertNull(resource);
        }

    @Test
    void loadCsvDataTable() throws IOException
        {
        MuseProject project = new SimpleProject();
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile("test_files/DataTable.csv", getClass())), new FactoryLocator(project.getClassLocator()), project.getClassLocator());
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof DataTable);

        DataTable table = (DataTable) resources.get(0);
        Assertions.assertEquals(2, table.getNumberColumns());
        Assertions.assertEquals("col1", table.getColumnNames()[0]);
        Assertions.assertEquals("col2", table.getColumnNames()[1]);
        Assertions.assertEquals(2, table.getNumberRows());
        Assertions.assertEquals("data1.1" , table.getData(0, 0));
        Assertions.assertEquals("data1.2" , table.getData("col2", 0));
        Assertions.assertEquals("data2.1" , table.getDataRow(1)[0]);
        Assertions.assertEquals("data2.2" , table.getDataRow(1)[1]);
        }
    }



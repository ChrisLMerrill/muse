package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.testutils.*;

import java.io.*;
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

        final List<ResourceToken> resources = project.getResourceStorage().findResources(new ResourceQueryParameters(new MuseTest.TestResourceType()));
        Assert.assertEquals(1, resources.size());
        Assert.assertEquals("test1", resources.get(0).getId());
        }

    @Test
    public void testOpenProjectWithFileResource()
        {
        FolderIntoMemoryResourceStorage store = new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/empty", getClass()));
        MuseProject project = new SimpleProject(store);
        MuseResource resource = project.getResourceStorage().getResource("not_done_yet", MockMuseResource.class);
        Assert.assertNull(resource);
        }

    @Test
    public void loadCsvDataTable() throws IOException
        {
        MuseProject project = new SimpleProject();
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile("test_files/DataTable.csv", getClass())), new FactoryLocator(project.getClassLocator()), project.getClassLocator());
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof DataTable);

        DataTable table = (DataTable) resources.get(0);
        Assert.assertEquals(2, table.getNumberColumns());
        Assert.assertEquals("col1", table.getColumnNames()[0]);
        Assert.assertEquals("col2", table.getColumnNames()[1]);
        Assert.assertEquals(2, table.getNumberRows());
        Assert.assertEquals("data1.1" , table.getData(0, 0));
        Assert.assertEquals("data1.2" , table.getData("col2", 0));
        Assert.assertEquals("data2.1" , table.getDataRow(1)[0]);
        Assert.assertEquals("data2.2" , table.getDataRow(1)[1]);
        }

    @Test
    public void loadTestPlugin() throws IOException
	    {
        MuseProject project = new SimpleProject();
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile("test_files/test-plugin-config.json", getClass())), new FactoryLocator(project.getClassLocator()), project.getClassLocator());
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof TestPluginsConfiguration);

	    TestPluginsConfiguration configs = (TestPluginsConfiguration) resources.get(0);
	    Assert.assertEquals(1, configs.getPlugins().size());
	    Assert.assertEquals(ValueSourceConfiguration.forValue(true), configs.getApplyToTestCondition());

	    TestPluginConfiguration config = configs.getPlugins().get(0);
	    Assert.assertEquals("type1", config.getTypeId());
	    ValueSourceConfiguration param1 = config.getSource("p1");
	    Assert.assertEquals(StringValueSource.TYPE_ID, param1.getType());
	    Assert.assertEquals("v1", param1.getValue());
	    Assert.assertEquals(ValueSourceConfiguration.forValue(false), config.getApplyCondition());
        }
    }



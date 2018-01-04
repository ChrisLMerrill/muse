package org.musetest.core.tests;

import org.junit.*;
import org.junit.rules.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class GenericResourceConfigurationTests
	{
	@Test
	public void saveAndLoadResource() throws IOException
		{
	    MockResourceConfig config = new MockResourceConfig();
	    config.setId("testMRC1");

	    SimpleProject project = new SimpleProject();
	    project.getResourceStorage().addResource(config);

	    // find by ID
		final ResourceToken found_by_id = project.getResourceStorage().findResource("testMRC1");
		Assert.assertEquals(config, found_by_id.getResource());

		// find by type
		final List<ResourceToken> found_by_type = project.getResourceStorage().findResources(new ResourceQueryParameters(config.getType()));
		Assert.assertEquals(config, found_by_type.get(0).getResource());
		}

	@Test
	public void serialization() throws IOException
		{
	    MockResourceConfig config = new MockResourceConfig();
	    config.parameters().addSource("param1", ValueSourceConfiguration.forValue("value1"));
	    config.parameters().addSource("paramB", ValueSourceConfiguration.forValue(false));
	    config.setId("testMRC2");

	    // store it to disk
		new FolderIntoMemoryResourceStorage(_project_folder.getRoot()).addResource(config);

		MuseResource from_disk = new FolderIntoMemoryResourceStorage(_project_folder.getRoot()).findResource("testMRC2").getResource();
		Assert.assertNotNull(from_disk);
		Assert.assertTrue(from_disk instanceof MockResourceConfig);
		MockResourceConfig restored = (MockResourceConfig) from_disk;
		Assert.assertEquals("testMRC2", restored.getId());
		Assert.assertEquals(2, restored.parameters().getSourceNames().size());
		Assert.assertEquals(ValueSourceConfiguration.forValue("value1"), restored.parameters().getSource("param1"));
		Assert.assertEquals(ValueSourceConfiguration.forValue(false), restored.parameters().getSource("paramB"));
		}


	@Rule
	public TemporaryFolder _project_folder = new TemporaryFolder();
	}
package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.values.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class GenericResourceConfigurationTests
	{
	@Test
    void saveAndLoadResource() throws IOException
		{
	    MockResourceConfig config = new MockResourceConfig();
	    config.setId("testMRC1");

	    SimpleProject project = new SimpleProject();
	    project.getResourceStorage().addResource(config);

	    // find by ID
		final ResourceToken found_by_id = project.getResourceStorage().findResource("testMRC1");
		Assertions.assertEquals(config, found_by_id.getResource());

		// find by type
		final List<ResourceToken> found_by_type = project.getResourceStorage().findResources(new ResourceQueryParameters(config.getType()));
		Assertions.assertEquals(config, found_by_type.get(0).getResource());
		}

	@Test
    void serialization() throws IOException
		{
	    MockResourceConfig config = new MockResourceConfig();
	    config.parameters().addSource("param1", ValueSourceConfiguration.forValue("value1"));
	    config.parameters().addSource("paramB", ValueSourceConfiguration.forValue(false));
	    config.setId("testMRC2");

	    // store it to disk
		new FolderIntoMemoryResourceStorage(_project_folder).addResource(config);

		MuseResource from_disk = new FolderIntoMemoryResourceStorage(_project_folder).findResource("testMRC2").getResource();
		Assertions.assertNotNull(from_disk);
		Assertions.assertTrue(from_disk instanceof MockResourceConfig);
		MockResourceConfig restored = (MockResourceConfig) from_disk;
		Assertions.assertEquals("testMRC2", restored.getId());
		Assertions.assertEquals(2, restored.parameters().getSourceNames().size());
		Assertions.assertEquals(ValueSourceConfiguration.forValue("value1"), restored.parameters().getSource("param1"));
		Assertions.assertEquals(ValueSourceConfiguration.forValue(false), restored.parameters().getSource("paramB"));
		}

    @BeforeEach
    void setup() throws IOException
        {
        _project_folder = Files.createTempDirectory("musetest").toFile();
        }

    @AfterEach
    void teardown()
        {
        _project_folder.deleteOnExit();
        }

    private File _project_folder;
	}
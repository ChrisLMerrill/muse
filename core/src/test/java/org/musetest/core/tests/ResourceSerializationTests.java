package org.musetest.core.tests;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.core.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.tests.mocks.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ResourceSerializationTests
	{
	@Test
	public void storeAndRestoreMetadata() throws IOException
		{
	    MockMuseResource resource = new MockMuseResource();
	    final String field_name = "NAME";
	    final String field_value = "value1";
		final String resource_id = "resource_id";
		resource.setId(resource_id);
	    resource.metadata().setMetadataField(field_name, field_value);

	    FolderIntoMemoryResourceStorage storage = new FolderIntoMemoryResourceStorage(_folder);
	    storage.addResource(resource);

		FolderIntoMemoryResourceStorage new_storage = new FolderIntoMemoryResourceStorage(_folder);
		MuseResource restored = new_storage.findResource(resource_id).getResource();
		Assertions.assertEquals(field_value, restored.metadata().getMetadataField(field_name));
	    }

	@BeforeEach
	public void setup() throws IOException
		{
		_folder = Files.createTempDirectory("muse-unit-test-").toFile();
		}

	@AfterEach
	public void teardown()
		{
		_folder.deleteOnExit();
		}

	private File _folder;
	}



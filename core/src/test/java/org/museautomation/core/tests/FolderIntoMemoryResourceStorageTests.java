package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.variables.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class FolderIntoMemoryResourceStorageTests
    {
    @SuppressWarnings("ConstantConditions")
    @Test
    void addAndDeleteResourceFile() throws IOException
        {
        Assertions.assertEquals(0, _folder.listFiles().length);
        MuseResource resource = createResource();

        // add a resource creates a file
        _project.getResourceStorage().addResource(resource);
        Assertions.assertEquals(1, _folder.listFiles().length);
        Assertions.assertTrue(_folder.listFiles()[0].getName().startsWith(resource.getId()));

        // removing resource deletes file...
        _project.getResourceStorage().removeResource(_project.getResourceStorage().findResource(resource.getId()));
        Assertions.assertEquals(0, _folder.listFiles().length);
        }

    @SuppressWarnings("ConstantConditions")
    @Test
    void overwriteExistingResourceFile() throws IOException
        {
        // create initial resource
        _project.getResourceStorage().addResource(createResource());

        // get timestamp and size of existing resource
        File file = _folder.listFiles()[0];
        long modified = file.lastModified();
        long size = file.length();

        // create a replacement
        MuseResource replacement = new VariableList();
        replacement.setId(RESOURCE_1_ID);

        // this should fail AND not modify the file
        try
            {
            _project.getResourceStorage().addResource(replacement);
            Assertions.fail("Exception should have been thrown");
            }
        catch (IllegalArgumentException e)
            {
            // expected
            }

        Assertions.assertEquals(modified, file.lastModified());
        Assertions.assertEquals(size, file.length());
        }

    @BeforeEach
    void setup() throws IOException
        {
        _folder = Files.createTempDirectory("museproject").toFile();
        _project = new SimpleProject(new FolderIntoMemoryResourceStorage(_folder));
        }

    @AfterEach
    void teardown()
        {
        _folder.deleteOnExit();
        }

    private MuseResource createResource()
        {
        MuseResource resource = new SteppedTest();
        resource.setId(RESOURCE_1_ID);
        return resource;
        }

    private SimpleProject _project;
    private File _folder;

    private final static String RESOURCE_1_ID = "resource1";
    }



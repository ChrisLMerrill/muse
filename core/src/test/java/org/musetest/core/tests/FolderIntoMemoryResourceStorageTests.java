package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.steptest.*;
import org.musetest.core.variables.*;

import java.io.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FolderIntoMemoryResourceStorageTests
    {
    @SuppressWarnings("ConstantConditions")
    @Test
    public void addAndDeleteResourceFile() throws IOException
        {
        Assert.assertEquals(0, _folder.listFiles().length);
        MuseResource resource = createResource();

        // add a resource creates a file
        _project.getResourceStorage().addResource(resource);
        Assert.assertEquals(1, _folder.listFiles().length);
        Assert.assertTrue(_folder.listFiles()[0].getName().startsWith(resource.getId()));

        // removing resource deletes file...
        _project.getResourceStorage().removeResource(_project.getResourceStorage().findResource(resource.getId()));
        Assert.assertEquals(0, _folder.listFiles().length);
        }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void overwriteExistingResourceFile() throws IOException
        {
        // create initial resource
        _project.getResourceStorage().addResource(createResource());

        // get timestamp and size of existing resource
        file = _folder.listFiles()[0];
        long modified = file.lastModified();
        long size = file.length();

        // create a replacement
        MuseResource replacement = new VariableList();
        replacement.setId(RESOURCE_1_ID);

        // this should fail AND not modify the file
        try
            {
            _project.getResourceStorage().addResource(replacement);
            Assert.assertTrue("Exception should have been thrown", false);
            }
        catch (IllegalArgumentException e)
            {
            // expected
            }

        Assert.assertEquals(modified, file.lastModified());
        Assert.assertEquals(size, file.length());
        }

    @Before
    public void setup() throws IOException
        {
        _folder = Files.createTempDirectory("museproject").toFile();
        _project = new SimpleProject(new FolderIntoMemoryResourceStorage(_folder));
        }

    @After
    public void teardown()
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
    private File file;

    public final static String RESOURCE_1_ID = "resource1";
    }



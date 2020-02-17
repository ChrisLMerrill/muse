package org.musetest.examples.tests;

import org.junit.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;
import org.museautomation.core.steptask.*;
import org.musetest.examples.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestMultiplyVariableByN
    {
    @Test
    public void testLoadingFromJSON() throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new StreamResourceOrigin(this.getClass().getClassLoader().getResourceAsStream("multiply-variable-by-n.json")));
        Assert.assertEquals(1, resources.size());
        MuseResource resource = resources.get(0);
        Assert.assertTrue(resource instanceof MuseTask);

        SteppedTask task = (SteppedTask) resource;
        TaskExecutionContext task_context = new DefaultTaskExecutionContext(new SimpleProject(), task);
        Assert.assertTrue(task.execute(task_context));
        }
    }




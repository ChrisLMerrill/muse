package org.musetest.examples.tests;

import org.junit.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.util.*;
import org.musetest.examples.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestIncrementVariableByN
    {
    @Test
    public void testLoadingFromJSON() throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new StreamResourceOrigin(this.getClass().getClassLoader().getResourceAsStream("increment-variable-by-n.json")));
        Assert.assertEquals(1, resources.size());
        MuseResource resource = resources.get(0);
        Assert.assertTrue(resource instanceof MuseTest);

        SteppedTest test = (SteppedTest) resource;
        TestExecutionContext test_context = new DefaultTestExecutionContext();
        MuseTestResult result = test.execute(test_context);
        Assert.assertTrue(result.isPass());
        }
    }




package org.musetest.examples.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.steptest.*;
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
        Assert.assertTrue(resource instanceof MuseTest);

		SteppedTest test = (SteppedTest) resource;
        TestExecutionContext test_context = new DefaultTestExecutionContext();
        MuseTestResult result = test.execute(test_context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());
        Assert.assertEquals(21L, test_context.getVariable("abc"));
        }
    }




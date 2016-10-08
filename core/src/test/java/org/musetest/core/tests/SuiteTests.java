package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.suite.*;
import org.musetest.tests.utils.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SuiteTests
    {
    @Test
    public void testSimpleSuiteById()
        {
        MuseProject project = ProjectFactory.create(TestUtils.getTestResource("projects/simpleSuite", getClass()), Collections.emptyMap());
        MuseTestSuite suite = project.findResource("TestSuite", MuseTestSuite.class);
        Assert.assertNotNull(suite);

        SimpleTestSuiteRunner runner = new SimpleTestSuiteRunner(suite);
        MuseTestSuiteResult result = runner.execute(project);
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(0, result.getErrorCount());
        }
    }



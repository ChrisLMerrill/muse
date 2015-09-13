package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.helpers.*;
import org.musetest.core.project.*;
import org.musetest.core.suite.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SuiteTests
    {
    @Test
    public void testSimpleSuiteById()
        {
        MuseProject project = ProjectFactory.create(TestUtils.getTestResource("projects/simpleSuite", getClass()));
        MuseTestSuite suite = project.findResource("TestSuite", MuseTestSuite.class);
        Assert.assertNotNull(suite);

        SimpleTestSuiteRunner runner = new SimpleTestSuiteRunner(suite);
        MuseTestSuiteResult result = runner.execute(project);
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(0, result.getErrorCount());
        }
    }



package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.steptest.*;
import org.musetest.core.suite.*;
import org.musetest.core.util.*;
import org.musetest.core.variables.*;
import org.musetest.tests.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestSuiteTests
    {
    @Test
    public void simpleSuite()
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());

        SimpleTestSuite suite = new SimpleTestSuite();
        suite.add(new MockTest());
        suite.add(new MockTest(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, "failed")));
        suite.add(new MockTest(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "errored")));

        Assert.assertEquals(3, suite.generateTestList(project).size());

        SimpleTestSuiteRunner runner = new SimpleTestSuiteRunner(suite);
        MuseTestSuiteResult result = runner.execute(project);
        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getErrorCount());
        }

    @Test
    public void loadTestSuiteFromJSON() throws IOException
        {
        File file = TestUtils.getTestResource("TestSuite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof IdListTestSuite);
        Assert.assertEquals(2, ((IdListTestSuite)resources.get(0)).getTestIds().size());
        }

    @Test
    public void loadParameterizedSuiteFromJson() throws IOException
        {
        File file = TestUtils.getTestResource("projects/parameterizedSuite/suite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof ParameterListTestSuite);
        ParameterListTestSuite suite = (ParameterListTestSuite) resources.get(0);
        Assert.assertEquals(3, suite.getParameters().size());

        Assert.assertEquals("checkSum", suite.getTestId());

        Assert.assertEquals(2L, suite.getParameters().get(0).get("x1"));
        Assert.assertEquals(5L, suite.getParameters().get(0).get("x2"));
        Assert.assertEquals(7L, suite.getParameters().get(0).get("sum"));

        Assert.assertEquals(1L, suite.getParameters().get(1).get("x1"));
        Assert.assertEquals(3L, suite.getParameters().get(1).get("x2"));
        Assert.assertEquals(4L, suite.getParameters().get(1).get("sum"));

        Assert.assertEquals(1L, suite.getParameters().get(2).get("x1"));
        Assert.assertEquals(0L, suite.getParameters().get(2).get("x2"));
        Assert.assertEquals(9L, suite.getParameters().get(2).get("sum"));
        }

    @Test
    public void paramterizedTestSuite() throws IOException
        {
        File suite_file = TestUtils.getTestResource("projects/parameterizedSuite/suite.json", this.getClass());
        MuseTestSuite suite = (MuseTestSuite) ResourceFactory.createResources(new FileResourceOrigin(suite_file), new FactoryLocator(null), DefaultClassLocator.get()).get(0);

        File test_file = TestUtils.getTestResource("projects/parameterizedSuite/checkSum.json", this.getClass());
        SteppedTest test = (SteppedTest) ResourceFactory.createResources(new FileResourceOrigin(test_file), new FactoryLocator(null), DefaultClassLocator.get()).get(0);

        SimpleProject project = new SimpleProject(new InMemoryResourceStorage());
        project.getResourceStorage().addResource(suite);
        project.getResourceStorage().addResource(test);

        SimpleTestSuiteRunner runner = new SimpleTestSuiteRunner(suite);
        MuseTestSuiteResult result = runner.execute(project);

        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(2, result.getSuccessCount());
        }
    }



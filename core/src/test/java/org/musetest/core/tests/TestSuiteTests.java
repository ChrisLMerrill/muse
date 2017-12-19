package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.suite.*;
import org.musetest.core.util.*;
import org.musetest.core.variables.*;
import org.musetest.testutils.*;

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

        MuseTestSuiteResult result = new SimpleTestSuiteRunner().execute(project, suite);
        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getErrorCount());
        }

    @Test
    public void executeSimpleSuiteById()
        {
        MuseProject project = ProjectFactory.create(TestResources.getFile("projects/simpleSuite", getClass()), Collections.emptyMap());
        MuseTestSuite suite = project.getResourceStorage().getResource("TestSuite", MuseTestSuite.class);
        Assert.assertNotNull(suite);

        MuseTestSuiteResult result = new SimpleTestSuiteRunner().execute(project, suite);
        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(1, result.getSuccessCount());
        Assert.assertEquals(0, result.getErrorCount());
        }

    @Test
    public void generateConfigWithSubsuites() throws IOException
        {
        MuseProject project = new SimpleProject();

        // create 2 tests
        MuseTest test1 = new MockTest("test1");
        project.getResourceStorage().addResource(test1);
        MuseTest test2 = new MockTest("test2");
        project.getResourceStorage().addResource(test2);

        // first suite contains the first test
        IdListTestSuite suite1 = new IdListTestSuite();
        suite1.setId("suite1");
        suite1.addTestId("test1");
        project.getResourceStorage().addResource(suite1);

        // second suite contains the second test...and the first suite
        IdListTestSuite suite2 = new IdListTestSuite();
        suite2.setId("suite2");
        suite2.addTestId("test2");
        suite2.addTestId("suite1");
        project.getResourceStorage().addResource(suite2);

        List<TestConfiguration> test_configs = suite2.generateTestList(project);

        Assert.assertEquals(2, test_configs.size());
        Assert.assertEquals(test2, test_configs.get(0).getTest());
        Assert.assertEquals(test1, test_configs.get(1).getTest());
        }

    @Test
    public void loadTestSuiteFromJSON() throws IOException
        {
        File file = TestResources.getFile("TestSuite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof IdListTestSuite);
        Assert.assertEquals(2, ((IdListTestSuite)resources.get(0)).getTestIds().size());
        }

    @Test
    public void loadParameterizedSuiteFromJson() throws IOException
        {
        File file = TestResources.getFile("projects/parameterizedSuite/suite.json", this.getClass());
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
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTestSuite suite = (MuseTestSuite) project.getResourceStorage().findResource("suite").getResource();
        MuseTestSuiteResult result = new SimpleTestSuiteRunner().execute(project, suite);

        Assert.assertEquals(1, result.getFailureCount());
        Assert.assertEquals(2, result.getSuccessCount());
        }

    @Test
    public void parametersFromCsv()
        {
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTestSuite suite = (MuseTestSuite) project.getResourceStorage().findResource("CsvSuite").getResource();
        MuseTestSuiteResult result = new SimpleTestSuiteRunner().execute(project, suite);

        Assert.assertEquals(3, result.getSuccessCount());
        }
    }



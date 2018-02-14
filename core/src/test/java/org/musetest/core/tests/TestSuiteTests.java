package org.musetest.core.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.suite.*;
import org.musetest.core.suite.plugins.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
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
    public void simpleSuite() throws IOException
	    {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
	    TestResultCollectorConfiguration result_collector = new TestResultCollectorConfiguration.TestResultCollectorConfigurationType().create();
	    result_collector.setId("result-collector");
	    project.getResourceStorage().addResource(result_collector);

        SimpleTestSuite suite = new SimpleTestSuite();

        final MockTest test1 = new MockTest();
        test1.setId("test1");
        project.getResourceStorage().addResource(test1);
	    suite.add(test1);

	    final MockTest test2 = new MockTest(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, "failed"));
	    test2.setId("test2");
        project.getResourceStorage().addResource(test2);
	    suite.add(test2);

	    final MockTest test3 = new MockTest(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, "errored"));
	    test3.setId("test3");
        project.getResourceStorage().addResource(test3);
	    suite.add(test3);

	    Assert.assertEquals(3L, suite.getTotalTestCount(project).longValue());

	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
	    new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));
	    Assert.assertNotNull(counter.getData());
        Assert.assertEquals(3, counter.getData().getTotalTests());
        Assert.assertEquals(1, counter.getData().getSuccesses());
        Assert.assertEquals(1, counter.getData().getFailures());
        Assert.assertEquals(1, counter.getData().getErrors());
        }

    @Test
    public void executeSimpleSuiteById()
	    {
        final File file = TestResources.getFile("projects/simpleSuite", getClass());
        if (file == null)
        	throw new IllegalArgumentException("simpleSuite folder is missing (test resource)");
        MuseProject project = ProjectFactory.create(file, Collections.emptyMap());
        MuseTestSuite suite = project.getResourceStorage().getResource("TestSuite", MuseTestSuite.class);
        Assert.assertNotNull(suite);

	    Assert.assertEquals(2L, suite.getTotalTestCount(project).longValue());

	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
        new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));
	    Assert.assertNotNull(counter.getData());
        Assert.assertEquals(1, counter.getData().getFailures());
        Assert.assertEquals(1, counter.getData().getSuccesses());
        Assert.assertEquals(0, counter.getData().getErrors());
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

        Assert.assertEquals(2L, suite2.getTotalTestCount(project).longValue());

        Iterator<TestConfiguration> tests = suite2.getTests(project);

        TestConfiguration config = tests.next();
        config.withinProject(project);
        Assert.assertEquals(test2, config.test());

        config = tests.next();
        config.withinProject(project);
        Assert.assertEquals(test1, config.test());
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
    public void parameterizedTestSuite()
	    {
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTestSuite suite = (MuseTestSuite) project.getResourceStorage().findResource("suite").getResource();
	    Assert.assertEquals(3L, suite.getTotalTestCount(project).longValue());
	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
        new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));

	    Assert.assertNotNull(counter.getData());
        Assert.assertEquals(1, counter.getData().getFailures());
        Assert.assertEquals(2, counter.getData().getSuccesses());
        }

    @Test
    public void parametersFromCsv()
	    {
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTestSuite suite = (MuseTestSuite) project.getResourceStorage().findResource("CsvSuite").getResource();
	    Assert.assertEquals(3L, suite.getTotalTestCount(project).longValue());
	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
        new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));

        Assert.assertNotNull(counter.getData());
        Assert.assertEquals(3, counter.getData().getSuccesses());
        }
    }
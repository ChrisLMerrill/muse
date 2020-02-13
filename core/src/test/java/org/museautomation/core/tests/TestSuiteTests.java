package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.suite.*;
import org.museautomation.core.suite.plugins.*;
import org.museautomation.core.test.*;
import org.museautomation.core.test.plugins.*;
import org.museautomation.core.util.*;
import org.museautomation.core.variables.*;
import org.museautomation.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TestSuiteTests
    {
    @Test
    void simpleSuite() throws IOException
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

	    Assertions.assertEquals(3L, suite.getTotalTestCount(project).longValue());

	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
	    new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));
	    Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(3, counter.getResult().getTotalTests());
        Assertions.assertEquals(1, counter.getResult().getSuccesses());
        Assertions.assertEquals(1, counter.getResult().getFailures());
        Assertions.assertEquals(1, counter.getResult().getErrors());
        }

    @Test
    void executeSimpleSuiteById()
	    {
        final File file = TestResources.getFile("projects/simpleSuite", getClass());
        if (file == null)
        	throw new IllegalArgumentException("simpleSuite folder is missing (test resource)");
        MuseProject project = ProjectFactory.create(file, Collections.emptyMap());
        MuseTestSuite suite = project.getResourceStorage().getResource("TestSuite", MuseTestSuite.class);
        Assertions.assertNotNull(suite);

	    Assertions.assertEquals(2L, suite.getTotalTestCount(project).longValue());

	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
        new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));
	    Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(1, counter.getResult().getFailures());
        Assertions.assertEquals(1, counter.getResult().getSuccesses());
        Assertions.assertEquals(0, counter.getResult().getErrors());
        }

    @Test
    void generateConfigWithSubsuites() throws IOException
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

        Assertions.assertEquals(2L, suite2.getTotalTestCount(project).longValue());

        Iterator<TestConfiguration> tests = suite2.getTests(project);

        TestConfiguration config = tests.next();
        config.withinContext(new ProjectExecutionContext(project));
        Assertions.assertEquals(test2, config.test());

        config = tests.next();
        config.withinContext(new ProjectExecutionContext(project));
        Assertions.assertEquals(test1, config.test());
        }

    @Test
    void loadTestSuiteFromJSON() throws IOException
        {
        File file = TestResources.getFile("TestSuite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof IdListTestSuite);
        Assertions.assertEquals(2, ((IdListTestSuite)resources.get(0)).getTestIds().size());
        }

    @Test
    void loadParameterizedSuiteFromJson() throws IOException
        {
        File file = TestResources.getFile("projects/parameterizedSuite/suite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof ParameterListTestSuite);
        ParameterListTestSuite suite = (ParameterListTestSuite) resources.get(0);
        Assertions.assertEquals(3, suite.getParameters().size());

        Assertions.assertEquals("checkSum", suite.getTestId());

        Assertions.assertEquals(2L, suite.getParameters().get(0).get("x1"));
        Assertions.assertEquals(5L, suite.getParameters().get(0).get("x2"));
        Assertions.assertEquals(7L, suite.getParameters().get(0).get("sum"));

        Assertions.assertEquals(1L, suite.getParameters().get(1).get("x1"));
        Assertions.assertEquals(3L, suite.getParameters().get(1).get("x2"));
        Assertions.assertEquals(4L, suite.getParameters().get(1).get("sum"));

        Assertions.assertEquals(1L, suite.getParameters().get(2).get("x1"));
        Assertions.assertEquals(0L, suite.getParameters().get(2).get("x2"));
        Assertions.assertEquals(9L, suite.getParameters().get(2).get("sum"));
        }

    @Test
    void parameterizedTestSuite()
	    {
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTestSuite suite = (MuseTestSuite) project.getResourceStorage().findResource("suite").getResource();
	    Assertions.assertEquals(3L, suite.getTotalTestCount(project).longValue());
	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
        new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));

	    Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(1, counter.getResult().getFailures());
        Assertions.assertEquals(2, counter.getResult().getSuccesses());
        }

    @Test
    void parametersFromCsv()
	    {
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTestSuite suite = (MuseTestSuite) project.getResourceStorage().findResource("CsvSuite").getResource();
	    Assertions.assertEquals(3L, suite.getTotalTestCount(project).longValue());
	    final TestSuiteResultCounter counter = new TestSuiteResultCounterConfiguration.TestSuiteResultCounterType().create().createPlugin();
        new SimpleTestSuiteRunner().execute(project, suite, Collections.singletonList(counter));

        Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(3, counter.getResult().getSuccesses());
        }
    }
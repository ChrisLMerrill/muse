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
import org.museautomation.core.task.*;
import org.museautomation.core.task.plugins.*;
import org.museautomation.core.util.*;
import org.museautomation.core.variables.*;
import org.museautomation.utils.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class TaskSuiteTests
    {
    @Test
    void simpleSuite() throws IOException
	    {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        TaskResultCollectorConfiguration result_collector = new TaskResultCollectorConfiguration.TaskResultCollectorConfigurationType().create();
	    result_collector.setId("result-collector");
	    project.getResourceStorage().addResource(result_collector);

        SimpleTaskSuite suite = new SimpleTaskSuite();

        final MockTask task1 = new MockTask();
        task1.setId("task1");
        project.getResourceStorage().addResource(task1);
	    suite.add(task1);

	    final MockTask task2 = new MockTask(new MuseTaskFailureDescription(MuseTaskFailureDescription.FailureType.Failure, "failed"));
	    task2.setId("task2");
        project.getResourceStorage().addResource(task2);
	    suite.add(task2);

	    final MockTask task3 = new MockTask(new MuseTaskFailureDescription(MuseTaskFailureDescription.FailureType.Error, "errored"));
	    task3.setId("task3");
        project.getResourceStorage().addResource(task3);
	    suite.add(task3);

	    Assertions.assertEquals(3L, suite.getTotalTaskCount(project).longValue());

	    final TaskSuiteResultCounter counter = new TaskSuiteResultCounterConfiguration.TaskSuiteResultCounterType().create().createPlugin();
	    new SimpleTaskSuiteRunner().execute(project, suite, Collections.singletonList(counter));
	    Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(3, counter.getResult().getTotalTasks());
        Assertions.assertEquals(1, counter.getResult().getSuccesses());
        Assertions.assertEquals(1, counter.getResult().getFailures());
        Assertions.assertEquals(1, counter.getResult().getErrors());
        }

    @Test
    void executeSimpleSuiteById()
	    {
        final File file = TestResources.getFile("projects/simpleSuite", getClass());
        if (file == null)
        	throw new IllegalArgumentException("simpleSuite folder is missing (task resource)");
        MuseProject project = ProjectFactory.create(file, Collections.emptyMap());
        MuseTaskSuite suite = project.getResourceStorage().getResource("TaskSuite", MuseTaskSuite.class);
        Assertions.assertNotNull(suite);

	    Assertions.assertEquals(2L, suite.getTotalTaskCount(project).longValue());

	    final TaskSuiteResultCounter counter = new TaskSuiteResultCounterConfiguration.TaskSuiteResultCounterType().create().createPlugin();
        new SimpleTaskSuiteRunner().execute(project, suite, Collections.singletonList(counter));
	    Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(1, counter.getResult().getFailures());
        Assertions.assertEquals(1, counter.getResult().getSuccesses());
        Assertions.assertEquals(0, counter.getResult().getErrors());
        }

    @Test
    void generateConfigWithSubsuites() throws IOException
        {
        MuseProject project = new SimpleProject();

        // create 2 tasks
        MuseTask task1 = new MockTask("task1");
        project.getResourceStorage().addResource(task1);
        MuseTask task2 = new MockTask("task2");
        project.getResourceStorage().addResource(task2);

        // first suite contains the first task
        IdListTaskSuite suite1 = new IdListTaskSuite();
        suite1.setId("suite1");
        suite1.addTaskId("task1");
        project.getResourceStorage().addResource(suite1);

        // second suite contains the second task...and the first suite
        IdListTaskSuite suite2 = new IdListTaskSuite();
        suite2.setId("suite2");
        suite2.addTaskId("task2");
        suite2.addTaskId("suite1");
        project.getResourceStorage().addResource(suite2);

        Assertions.assertEquals(2L, suite2.getTotalTaskCount(project).longValue());

        Iterator<TaskConfiguration> tasks = suite2.getTasks(project);

        TaskConfiguration config = tasks.next();
        config.withinContext(new ProjectExecutionContext(project));
        Assertions.assertEquals(task2, config.task());

        config = tasks.next();
        config.withinContext(new ProjectExecutionContext(project));
        Assertions.assertEquals(task1, config.task());
        }

    @Test
    void loadTaskSuiteFromJSON() throws IOException
        {
        File file = TestResources.getFile("TaskSuite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof IdListTaskSuite);
        Assertions.assertEquals(2, ((IdListTaskSuite)resources.get(0)).getTaskIds().size());
        }

    @Test
    void loadParameterizedSuiteFromJson() throws IOException
        {
        File file = TestResources.getFile("projects/parameterizedSuite/suite.json", this.getClass());
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(file), new FactoryLocator(null), DefaultClassLocator.get());

        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof ParameterListTaskSuite);
        ParameterListTaskSuite suite = (ParameterListTaskSuite) resources.get(0);
        Assertions.assertEquals(3, suite.getParameters().size());

        Assertions.assertEquals("checkSum", suite.getTaskId());

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
        MuseTaskSuite suite = (MuseTaskSuite) project.getResourceStorage().findResource("suite").getResource();
	    Assertions.assertEquals(3L, suite.getTotalTaskCount(project).longValue());
	    final TaskSuiteResultCounter counter = new TaskSuiteResultCounterConfiguration.TaskSuiteResultCounterType().create().createPlugin();
        new SimpleTaskSuiteRunner().execute(project, suite, Collections.singletonList(counter));

	    Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(1, counter.getResult().getFailures());
        Assertions.assertEquals(2, counter.getResult().getSuccesses());
        }

    @Test
    void parametersFromCsv()
	    {
        MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(TestResources.getFile("projects/parameterizedSuite", this.getClass())));
        MuseTaskSuite suite = (MuseTaskSuite) project.getResourceStorage().findResource("CsvSuite").getResource();
	    Assertions.assertEquals(3L, suite.getTotalTaskCount(project).longValue());
	    final TaskSuiteResultCounter counter = new TaskSuiteResultCounterConfiguration.TaskSuiteResultCounterType().create().createPlugin();
        new SimpleTaskSuiteRunner().execute(project, suite, Collections.singletonList(counter));

        Assertions.assertNotNull(counter.getResult());
        Assertions.assertEquals(3, counter.getResult().getSuccesses());
        }
    }
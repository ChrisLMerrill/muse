package org.museautomation.javascript.tests;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.origin.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.step.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.steptest.*;
import org.museautomation.core.tests.utils.*;
import org.museautomation.core.values.*;
import org.museautomation.javascript.*;
import org.museautomation.javascript.factory.*;
import org.museautomation.utils.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class JavascriptTests
    {
    @Test
    void javascriptTestSuccess()
	    {
	    MuseTest test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_SUCCESS; } "));
	    test.setId("javascript-test");
	    TestResult result = TestRunHelper.runTest(new SimpleProject(), test);
	    Assertions.assertTrue(result.isPass());
	    }

    @Test
    void javascriptTestFailure()
        {
        MuseTest test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return \"things went badly\"; } "));
        test.setId("javascript-test");
        TestResult result = TestRunHelper.runTest(new SimpleProject(), test);
        Assertions.assertEquals(TestResult.FailureType.Failure, result.getFailures().get(0).getType());
        }

    @Test
    void javascriptNotATest() throws ScriptException
        {
        TestFromJavascriptResourceFactory factory = new TestFromJavascriptResourceFactory();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        String script = "function not_the_right_method(test_context) { return TEST_SUCCESS; } ";
        engine.eval(script);
        List<MuseResource> resources = factory.createResources(new StringResourceOrigin("abc"), new MuseTest.TestResourceType(), engine);
        Assertions.assertEquals(0, resources.size());
        }

    @Test
    void loadJavascriptTestFromFile() throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile("javascriptTest.js", this.getClass())));
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof MuseTest);
        TestResult result = TestRunHelper.runTest(new SimpleProject(), (MuseTest) resources.get(0));
        Assertions.assertTrue(result.isPass());
        }

    @Test
    void loadJavascriptStepFromFile() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "javascriptStep.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        config.addSource("param1", ValueSourceConfiguration.forValue("XYZ"));

        MuseStep step = config.createStep(project);
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, step.execute(new MockStepExecutionContext()).getStatus());

        StepDescriptor descriptor = project.getStepDescriptors().get(config);
        Assertions.assertNotNull(descriptor);

        Assertions.assertEquals("javascriptStep", descriptor.getType());
        Assertions.assertEquals("JS Example", descriptor.getName());
        Assertions.assertEquals("javascript", descriptor.getGroupName());
        Assertions.assertEquals("glyph:FontAwesome:PAW", descriptor.getIconDescriptor());
        Assertions.assertEquals("A Javascript step", descriptor.getShortDescription());
        Assertions.assertEquals("The long description of the javascript step", descriptor.getLongDescription());

        // TODO this does not yet pass...need to convert the config to a JS object to pass into a function returned by the descriptor
//        Assert.assertEquals("Do something with XYZ", descriptor.getShortDescription(config));
        }

    private JavascriptStepResource loadJavascriptStepFromFileIntoProject(MuseProject project, String filename) throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile(filename, this.getClass())));
        Assertions.assertEquals(1, resources.size());
        Assertions.assertTrue(resources.get(0) instanceof JavascriptStepResource);

        JavascriptStepResource step_resource = (JavascriptStepResource) resources.get(0);
        project.getResourceStorage().addResource(step_resource);

        return step_resource;
        }

    @Test
    void getAndSetVariablesInJavascriptStep() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "getSetVariables.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        MuseStep step = config.createStep(project);
        MockStepExecutionContext context = new MockStepExecutionContext();
        context.setVariable("var_in", "input");
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        Assertions.assertEquals("output", context.getVariable("var_out"));
        }

    @Test
    void accessValueSourcesInJavascriptStep() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "AccessSources.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        config.addSource("named_source", ValueSourceConfiguration.forValue("named_value"));
        MuseStep step = config.createStep(project);
        MockStepExecutionContext context = new MockStepExecutionContext();
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        }

    @Test
    void logMessageInJavascriptStep() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "LogMessage.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        MuseStep step = config.createStep(project);

        final List<MuseEvent> events = new ArrayList<>();
        SteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(project, new SteppedTest(new StepConfiguration("mock-step")));
        StepExecutionContext context = new SingleStepExecutionContext(test_context, config, true);
        test_context.addEventListener(event ->
            {
            if (event.getTypeId().equals(MessageEventType.TYPE_ID))
                events.add(event);
            });

        Assertions.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        Assertions.assertEquals(1, events.size());
        Assertions.assertTrue(EventTypes.DEFAULT.findType(events.get(0)).getDescription(events.get(0)).contains("test message"));
        }

    @Test
    void evaluateJavascriptValueSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forTypeWithSource(EvaluateJavascriptValueSource.TYPE_ID, "'abc' + 1;");
        SimpleProject project = new SimpleProject();
        MuseValueSource source = config.createSource(project);
        Object result = source.resolveValue(new MockStepExecutionContext(project));
        Assertions.assertEquals("abc1", result);
        }
    }
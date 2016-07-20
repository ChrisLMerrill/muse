package org.musetest.javascript.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;
import org.musetest.javascript.*;
import org.musetest.javascript.factory.*;

import javax.script.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JavascriptTests
    {
    @Test
    public void javascriptTest()
        {
        TestExecutionContext context = new DefaultTestExecutionContext();

        MuseTest test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_SUCCESS; } "));
        MuseTestResult result = test.execute(context);
        Assert.assertTrue(result.isPass());

        test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_FAILURE; } "));
        result = test.execute(context);
        Assert.assertEquals(MuseTestFailureDescription.FailureType.Failure, result.getFailureDescription().getFailureType());
        }

    @Test
    public void javascriptNotATest() throws ScriptException, IOException
        {
        TestFromJavascriptResourceFactory factory = new TestFromJavascriptResourceFactory();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        String script = "function not_the_right_method(test_context) { return TEST_SUCCESS; } ";
        engine.eval(script);
        List<MuseResource> resources = factory.createResources(new StringResourceOrigin("abc"), ResourceTypes.Test, engine);
        Assert.assertEquals(0, resources.size());
        }

    @Test
    public void loadJavascriptTestFromFile() throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(org.musetest.core.helpers.TestUtils.getTestResource("javascriptTest.js", this.getClass())));
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof MuseTest);
        MuseTestResult result = ((MuseTest) resources.get(0)).execute(new DefaultTestExecutionContext());
        Assert.assertTrue(result.isPass());
        }

    @Test
    public void loadJavascriptStepFromFile() throws IOException, StepExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "javascriptStep.js");

        StepConfiguration config = new StepConfiguration(step_resource.getMetadata().getId());
        config.setSource("param1", ValueSourceConfiguration.forValue("XYZ"));

        MuseStep step = config.createStep(project);
        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(new DummyStepExecutionContext()).getStatus());

        StepDescriptor descriptor = project.getStepDescriptors().get(config);
        Assert.assertNotNull(descriptor);

        Assert.assertEquals("javascriptStep", descriptor.getType());
        Assert.assertEquals("JS Example", descriptor.getName());
        Assert.assertEquals("javascript", descriptor.getGroupName());
        Assert.assertEquals("glyph:FontAwesome:PAW", descriptor.getIconDescriptor());
        Assert.assertEquals("A Javascript step", descriptor.getShortDescription());
        Assert.assertEquals("The long description of the javascript step", descriptor.getLongDescription());

        // TODO this does not yet pass...need to convert the config to a JS object to pass into a function returned by the descriptor
//        Assert.assertEquals("Do something with XYZ", descriptor.getShortDescription(config));
        }

    private JavascriptStepResource loadJavascriptStepFromFileIntoProject(MuseProject project, String filename) throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(org.musetest.core.helpers.TestUtils.getTestResource(filename, this.getClass())));
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof JavascriptStepResource);

        JavascriptStepResource step_resource = (JavascriptStepResource) resources.get(0);
        project.addResource(step_resource);

        return step_resource;
        }

    @Test
    public void getAndSetVariablesInJavascriptStep() throws IOException, StepExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "getSetVariables.js");

        StepConfiguration config = new StepConfiguration(step_resource.getMetadata().getId());
        MuseStep step = config.createStep(project);
        DummyStepExecutionContext context = new DummyStepExecutionContext();
        context.setLocalVariable("var_in", "input");
        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());

        Assert.assertEquals("output", context.getLocalVariable("var_out"));
        }

    @Test
    public void accessValueSourcesInJavascriptStep() throws IOException, StepExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "AccessSources.js");

        StepConfiguration config = new StepConfiguration(step_resource.getMetadata().getId());
        config.setSource("named_source", ValueSourceConfiguration.forValue("named_value"));
        MuseStep step = config.createStep(project);
        DummyStepExecutionContext context = new DummyStepExecutionContext();
        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        }

    @Test
    public void logMessageInJavascriptStep() throws IOException, StepExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "LogMessage.js");

        StepConfiguration config = new StepConfiguration(step_resource.getMetadata().getId());
        MuseStep step = config.createStep(project);

        final List<MessageEvent> events = new ArrayList<>();
        StepExecutionContext context = new SingleStepExecutionContext(new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext()), config, true);
        context.getTestExecutionContext().addEventListener(event ->
            {
            if (event instanceof MessageEvent)
                events.add((MessageEvent) event);
            });

        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        Assert.assertEquals(1, events.size());
        Assert.assertTrue(events.get(0).getDescription().contains("test message"));
        }
    }



package org.musetest.javascript.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;
import org.musetest.javascript.*;
import org.musetest.javascript.factory.*;
import org.musetest.testutils.*;

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
        MuseTest test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_SUCCESS; } "));
        TestExecutionContext context = new DefaultTestExecutionContext(new SimpleProject(), test);
        MuseTestResult result = test.execute(context);
        Assert.assertTrue(result.isPass());

        test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_FAILURE; } "));
        context = new DefaultTestExecutionContext(new SimpleProject(), test);
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
        List<MuseResource> resources = factory.createResources(new StringResourceOrigin("abc"), new MuseTest.TestResourceType(), engine);
        Assert.assertEquals(0, resources.size());
        }

    @Test
    public void loadJavascriptTestFromFile() throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile("javascriptTest.js", this.getClass())));
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof MuseTest);
        MuseTestResult result = ((MuseTest) resources.get(0)).execute(new DefaultTestExecutionContext(new SimpleProject(), null));
        Assert.assertTrue(result.isPass());
        }

    @Test
    public void loadJavascriptStepFromFile() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "javascriptStep.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        config.addSource("param1", ValueSourceConfiguration.forValue("XYZ"));

        MuseStep step = config.createStep(project);
        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(new MockStepExecutionContext()).getStatus());

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
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(TestResources.getFile(filename, this.getClass())));
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof JavascriptStepResource);

        JavascriptStepResource step_resource = (JavascriptStepResource) resources.get(0);
        project.getResourceStorage().addResource(step_resource);

        return step_resource;
        }

    @Test
    public void getAndSetVariablesInJavascriptStep() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "getSetVariables.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        MuseStep step = config.createStep(project);
        MockStepExecutionContext context = new MockStepExecutionContext();
        context.setVariable("var_in", "input");
        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        Assert.assertEquals("output", context.getVariable("var_out"));
        }

    @Test
    public void accessValueSourcesInJavascriptStep() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "AccessSources.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        config.addSource("named_source", ValueSourceConfiguration.forValue("named_value"));
        MuseStep step = config.createStep(project);
        MockStepExecutionContext context = new MockStepExecutionContext();
        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        }

    @Test
    public void logMessageInJavascriptStep() throws IOException, MuseExecutionError
        {
        MuseProject project = new SimpleProject(new InMemoryResourceStorage());
        JavascriptStepResource step_resource = loadJavascriptStepFromFileIntoProject(project, "LogMessage.js");

        StepConfiguration config = new StepConfiguration(step_resource.getId());
        MuseStep step = config.createStep(project);

        final List<MuseEvent> events = new ArrayList<>();
        DefaultSteppedTestExecutionContext test_context = new DefaultSteppedTestExecutionContext(new DefaultTestExecutionContext(project, null));
        StepExecutionContext context = new SingleStepExecutionContext(test_context, config, true);
        test_context.addEventListener(event ->
            {
            if (event.getTypeId().equals(MessageEventType.TYPE_ID))
                events.add(event);
            });

        Assert.assertEquals(StepExecutionStatus.COMPLETE, step.execute(context).getStatus());
        Assert.assertEquals(1, events.size());
        Assert.assertTrue(EventTypes.DEFAULT.findType(events.get(0)).getDescription(events.get(0)).contains("test message"));
        }

    @Test
    public void evaluateJavascriptValueSource() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forTypeWithSource(EvaluateJavascriptValueSource.TYPE_ID, "'abc' + 1;");
        SimpleProject project = new SimpleProject();
        MuseValueSource source = config.createSource(project);
        Object result = source.resolveValue(new MockStepExecutionContext(project));
        Assert.assertEquals("abc1", result);
        }
    }
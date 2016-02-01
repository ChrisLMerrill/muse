package org.musetest.javascript.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.origin.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
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
    public void testJavascriptTest()
        {
        TestExecutionContext context = new DefaultTestExecutionContext();

        MuseTest test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_SUCCESS; } "));
        MuseTestResult result = test.execute(context);
        Assert.assertEquals(MuseTestResultStatus.Success, result.getStatus());

        test = new JavascriptTest(new StringResourceOrigin("function executeTest(test_context) { return TEST_FAILURE; } "));
        result = test.execute(context);
        Assert.assertEquals(MuseTestResultStatus.Failure, result.getStatus());
        }

    @Test
    public void testJavascriptNotATest() throws ScriptException, IOException
        {
        TestFromJavascriptResourceFactory factory = new TestFromJavascriptResourceFactory();
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("javascript");
        String script = "function not_the_right_method(test_context) { return TEST_SUCCESS; } ";
        engine.eval(script);
        List<MuseResource> resources = factory.createResources(new StringResourceOrigin("abc"), ResourceTypes.Test, engine);
        Assert.assertEquals(0, resources.size());
        }

    @Test
    public void testJavascriptTestLoadFromFile() throws IOException
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(org.musetest.core.helpers.TestUtils.getTestResource("javascriptTest.js", this.getClass())));
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof MuseTest);
        Assert.assertEquals(MuseTestResultStatus.Success, ((MuseTest) resources.get(0)).execute(new DefaultTestExecutionContext()).getStatus());
        }

    @Test
    public void testJavascriptStepLoadFromFile() throws IOException, StepExecutionError
        {
        List<MuseResource> resources = ResourceFactory.createResources(new FileResourceOrigin(org.musetest.core.helpers.TestUtils.getTestResource("javascriptStep.js", this.getClass())));
        Assert.assertEquals(1, resources.size());
        Assert.assertTrue(resources.get(0) instanceof JavascriptStepResource);

        JavascriptStepResource step_resource = (JavascriptStepResource) resources.get(0);

        StepConfiguration config = new StepConfiguration(step_resource.getMetadata().getId());
        config.addSource("param1", ValueSourceConfiguration.forValue("XYZ"));

        MuseProject project = new SimpleProject(new InMemoryResourceStore());
        project.addResource(step_resource);

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

    }



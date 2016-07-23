package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.tests.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.mocks.*;
import org.musetest.selenium.pages.*;
import org.musetest.selenium.steps.*;
import org.openqa.selenium.*;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SeleniumStepTests
    {
    @Test
    public void openBrowser() throws ValueSourceResolutionError
        {
        TestExecutionContext context = runTestWithSteps(createOpenBrowserStep());
        MuseMockDriver driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(context);
        Assert.assertNotNull(driver);
        }

    @Test
    public void openAndCloseBrowser() throws ValueSourceResolutionError
        {
        TestExecutionContext context = runTestWithSteps(createOpenBrowserStep(), new StepConfiguration(CloseBrowser.TYPE_ID));
        MuseMockDriver driver;
        try
            {
            driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(context);
            Assert.assertNull(driver);
            }
        catch (ValueSourceResolutionError valueSourceResolutionError)
            {
            // ok, this is what we expect.
            }
        }

    @Test
    public void gotoUrl() throws ValueSourceResolutionError
        {
        final String URL = "thetesturl";
        StepConfiguration goto_url = new StepConfiguration(GotoUrl.TYPE_ID);
        goto_url.setSource(GotoUrl.URL_PARAM, ValueSourceConfiguration.forValue(URL));
        TestExecutionContext context = runTestWithSteps(createOpenBrowserStep(), goto_url);
        MuseMockDriver driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(context);
        Assert.assertEquals(URL, driver.getCurrentUrl());
        }

    @Test
    public void clickElement() throws StepExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String id = "element#1";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addIdElement(id, element1);

        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration click = new StepConfiguration(ClickElement.TYPE_ID);
        click.setSource(ClickElement.ELEMENT_PARAM, ValueSourceConfiguration.forSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id)));
        MuseStep step = click.createStep();
        StepExecutionResult result = step.execute(context);
        Assert.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        Assert.assertTrue(element1.isClicked());
        }

    @Test
    public void switchToFrame() throws StepExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String id = "frame#1";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addIdElement(id, element1);

        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration switch_to = new StepConfiguration(SwitchTo.TYPE_ID);
        switch_to.setSource(SwitchTo.TARTGET_PARAM, ValueSourceConfiguration.forSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id)));
        MuseStep step = switch_to.createStep();
        StepExecutionResult result = step.execute(context);
        Assert.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());

        Assert.assertEquals(element1, driver.getTarget());
        }

    /**
     * Note that this test doesnt actually execute a script - just signals the mock driver to not fail
     */
    @Test
    public void executeScript() throws StepExecutionError
        {
        StepExecutionResult result = executeScriptStep(new ScriptableMockDriver(), "a valid script");
        Assert.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        }

    /**
     * Note that this test doesnt actually execute a script - just signals the mock browser to throw an exception so we can test that it is handled
     */
    @Test
    public void executeScriptThrowsExeception() throws StepExecutionError
        {
        StepExecutionResult result = executeScriptStep(new ScriptableMockDriver(), ScriptableMockDriver.THROW_EXCEPTION);
        Assert.assertEquals(StepExecutionStatus.ERROR, result.getStatus());
        }

    /**
     * Note that this test doesnt actually execute a script - just ensures we handle the condition correctly
     */
    @Test
    public void executeScriptOnNonscriptableBrowser() throws StepExecutionError
        {
        StepExecutionError error = null;
        try
            {
            executeScriptStep(new MuseMockDriver(), "this would be the script");
            }
        catch (StepExecutionError stepExecutionError)
            {
            error = stepExecutionError;
            }
        Assert.assertNotNull(error);
        }

    private StepExecutionResult executeScriptStep(WebDriver driver, String script) throws StepExecutionError
        {
        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration execute = new StepConfiguration(ExecuteJavascript.TYPE_ID);
        execute.setSource(ExecuteJavascript.SCRIPT_PARAM, ValueSourceConfiguration.forValue(script));
        MuseStep step = execute.createStep();
        return step.execute(context);
        }

    @Test
    public void switchToUnknownType() throws StepExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();

        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration switch_to = new StepConfiguration(SwitchTo.TYPE_ID);
        switch_to.setSource(SwitchTo.TARTGET_PARAM, ValueSourceConfiguration.forValue(true)); // not a valid type
        MuseStep step = switch_to.createStep(null);
        StepExecutionResult result = step.execute(context);
        Assert.assertEquals(StepExecutionStatus.FAILURE, result.getStatus());
        }

    @Test
    public void locateElementWithPageMap() throws StepExecutionError
        {
        MuseProject project = new SimpleProject();

        WebPage page1 = new WebPage();
        page1.getMetadata().setId("page1");
        page1.getMetadata().setType(new ResourceTypes(DefaultClassLocator.get()).forObject(page1));
        PageElement element1 = new PageElement();
        final String element_id = "element#1";
        element1.setLocator(ValueSourceConfiguration.forSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(element_id)));
        String page_element_id = "element1";
        page1.addElement(page_element_id, element1);
        project.addResource(page1);

        MuseMockDriver driver = new MuseMockDriver();
        final MuseMockElement mock_element1 = new MuseMockElement();
        driver.addIdElement(element_id, mock_element1);

        StepExecutionContext context = new DummyStepExecutionContext(project);
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration click = new StepConfiguration(ClickElement.TYPE_ID);
        ValueSourceConfiguration element_source = ValueSourceConfiguration.forType(PagesElementValueSource.TYPE_ID);
        element_source.addSource(PagesElementValueSource.PAGE_PARAM_ID, ValueSourceConfiguration.forValue(page1.getMetadata().getId()));
        element_source.addSource(PagesElementValueSource.ELEMENT_PARAM_ID, ValueSourceConfiguration.forValue(page_element_id));
        click.setSource(ClickElement.ELEMENT_PARAM, element_source);
        MuseStep step = click.createStep(null);
        step.execute(context);

        Assert.assertTrue(mock_element1.isClicked());
        }

    private TestExecutionContext runTestWithSteps(StepConfiguration... steps)
        {
        MuseProject project = createSeleniumTestProject();
        SteppedTest test;
        if (steps.length == 1)
            test = new SteppedTest(steps[0]);
        else
            {
            StepConfiguration root = new StepConfiguration(BasicCompoundStep.TYPE_ID);
            for (StepConfiguration step : steps)
                root.addChild(step);
            test = new SteppedTest(root);
            }

        DefaultTestExecutionContext context = new DefaultTestExecutionContext(project);
        test.execute(context);
        return context;
        }

    static MuseProject createSeleniumTestProject()
        {
        if (PROJECT == null)
            {
            File file = null;
            try
                {
                file = new File(SeleniumStepTests.class.getResource("driver-providers.json").toURI());
                }
            catch (URISyntaxException e)
                {
                Assert.assertTrue("illegal syntax in File-to-URL conversion ??", false);
                }
            File folder = file.getParentFile();
            MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStore(folder));
            project.open();
            PROJECT = project;
            }
        return PROJECT;
        }

    // until project loading is faster, only create this once
    private static MuseProject PROJECT = null;

    static StepConfiguration createOpenBrowserStep()
        {
        StepConfiguration open_browser_step = new StepConfiguration(OpenBrowser.TYPE_ID);
        ValueSourceConfiguration provider_source = ValueSourceConfiguration.forTypeWithSource(ProjectResourceValueSource.TYPE_ID, ValueSourceConfiguration.forValue("driver-providers"));
        open_browser_step.setSource(OpenBrowser.PROVIDER_PARAM, provider_source);
        ValueSourceConfiguration browser_source = ValueSourceConfiguration.forTypeWithSource(ProjectResourceValueSource.TYPE_ID, ValueSourceConfiguration.forValue("capabilities-musemock"));
        open_browser_step.setSource(OpenBrowser.BROWSER_PARAM, browser_source);
        return open_browser_step;
        }
    }



package org.museautomation.selenium.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.storage.*;
import org.museautomation.core.step.*;
import org.museautomation.core.steptask.*;
import org.museautomation.core.values.*;
import org.museautomation.selenium.*;
import org.museautomation.selenium.locators.*;
import org.museautomation.selenium.mocks.*;
import org.museautomation.selenium.providers.*;
import org.museautomation.selenium.steps.*;
import org.openqa.selenium.*;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class SeleniumStepTests
    {
    @Test
    void openBrowser() throws ValueSourceResolutionError
        {
        SteppedTaskExecutionContext context = runTestWithSteps(createOpenBrowserStep());
        MuseMockDriver driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(context);
        Assertions.assertNotNull(driver);
        }

    @Test
    void openAndCloseBrowser()
	    {
	    SteppedTaskExecutionContext context = runTestWithSteps(createOpenBrowserStep(), new StepConfiguration(CloseBrowser.TYPE_ID));
        MuseMockDriver driver;
        try
            {
            driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(context);
            Assertions.assertNull(driver);
            }
        catch (ValueSourceResolutionError valueSourceResolutionError)
            {
            // ok, this is what we expect.
            }
        }

    @Test
    void gotoUrl() throws ValueSourceResolutionError
        {
        final String URL = "thetesturl";
        StepConfiguration goto_url = new StepConfiguration(GotoUrl.TYPE_ID);
        goto_url.addSource(GotoUrl.URL_PARAM, ValueSourceConfiguration.forValue(URL));
        SteppedTaskExecutionContext context = runTestWithSteps(createOpenBrowserStep(), goto_url);
        MuseMockDriver driver = (MuseMockDriver) BrowserStepExecutionContext.getDriver(context);
        Assertions.assertEquals(URL, driver.getCurrentUrl());
        }

    @Test
    void clickElement() throws MuseExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String id = "element#1";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addIdElement(id, element1);

        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration click = new StepConfiguration(ClickElement.TYPE_ID);
        click.addSource(ClickElement.ELEMENT_PARAM, ValueSourceConfiguration.forSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id)));
        MuseStep step = click.createStep();
        StepExecutionResult result = step.execute(context);
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        Assertions.assertTrue(element1.isClicked());
        }

    @Test
    void switchToFrame() throws MuseExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String id = "frame#1";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addIdElement(id, element1);

        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration switch_to = new StepConfiguration(SwitchTo.TYPE_ID);
        switch_to.addSource(SwitchTo.TARGET_PARAM, ValueSourceConfiguration.forSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id)));
        MuseStep step = switch_to.createStep();
        StepExecutionResult result = step.execute(context);
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());

        Assertions.assertEquals(element1, driver.getTarget());
        }

    /**
     * Note that this test doesnt actually execute a script - just signals the mock driver to not fail
     */
    @Test
    void executeScript() throws MuseExecutionError
        {
        StepExecutionResult result = executeScriptStep(new ScriptableMockDriver(), "a valid script");
        Assertions.assertEquals(StepExecutionStatus.COMPLETE, result.getStatus());
        }

    /**
     * Note that this test doesnt actually execute a script - just signals the mock browser to throw an exception so we can test that it is handled
     */
    @Test
    void executeScriptThrowsExeception() throws MuseExecutionError
        {
        StepExecutionResult result = executeScriptStep(new ScriptableMockDriver(), ScriptableMockDriver.THROW_EXCEPTION);
        Assertions.assertEquals(StepExecutionStatus.ERROR, result.getStatus());
        }

    /**
     * Note that this test doesnt actually execute a script - just ensures we handle the condition correctly
     */
    @Test
    void executeScriptOnNonscriptableBrowser() throws MuseExecutionError
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
        Assertions.assertNotNull(error);
        }

    private StepExecutionResult executeScriptStep(WebDriver driver, String script) throws MuseExecutionError
        {
        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration execute = new StepConfiguration(ExecuteJavascriptStep.TYPE_ID);
        execute.addSource(ExecuteJavascriptStep.SCRIPT_PARAM, ValueSourceConfiguration.forValue(script));
        MuseStep step = execute.createStep();
        return step.execute(context);
        }

    @Test
    void switchToUnknownType() throws MuseExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();

        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration switch_to = new StepConfiguration(SwitchTo.TYPE_ID);
        switch_to.addSource(SwitchTo.TARGET_PARAM, ValueSourceConfiguration.forValue(true)); // not a valid type
        MuseStep step = switch_to.createStep(null);
        StepExecutionResult result = step.execute(context);
        Assertions.assertEquals(StepExecutionStatus.FAILURE, result.getStatus());
        }

    private SteppedTaskExecutionContext runTestWithSteps(StepConfiguration... steps)
        {
        MuseProject project = createSeleniumTestProject();
        SteppedTask test;
        if (steps.length == 1)
            test = new SteppedTask(steps[0]);
        else
            {
            StepConfiguration root = new StepConfiguration(BasicCompoundStep.TYPE_ID);
            for (StepConfiguration step : steps)
                root.addChild(step);
            test = new SteppedTask(root);
            }

        SteppedTaskExecutionContext context = new DefaultSteppedTaskExecutionContext(project, test);
        test.execute(context);
        return context;
        }

    private static MuseProject createSeleniumTestProject()
        {
        if (PROJECT == null)
            {
            File file = null;
            try
                {
                file = new File(WebDriverProviderTests.class.getResource("driver-providers.json").toURI());
                }
            catch (URISyntaxException e)
                {
                Assertions.fail("illegal syntax in File-to-URL conversion ??");
                }
            File folder = file.getParentFile();
            MuseProject project = new SimpleProject(new FolderIntoMemoryResourceStorage(folder));
            project.open();
            PROJECT = project;
            }
        return PROJECT;
        }

    // until project loading is faster, only create this once
    private static MuseProject PROJECT = null;

    private static StepConfiguration createOpenBrowserStep()
        {
        StepConfiguration open_browser_step = new StepConfiguration(OpenBrowser.TYPE_ID);
        ValueSourceConfiguration provider_source = ValueSourceConfiguration.forTypeWithSource(ProjectResourceValueSource.TYPE_ID, ValueSourceConfiguration.forValue("driver-providers"));
        open_browser_step.addSource(OpenBrowser.PROVIDER_PARAM, provider_source);
        ValueSourceConfiguration browser_source = ValueSourceConfiguration.forTypeWithSource(ProjectResourceValueSource.TYPE_ID, ValueSourceConfiguration.forValue("capabilities-musemock"));
        open_browser_step.addSource(OpenBrowser.BROWSER_PARAM, browser_source);
        return open_browser_step;
        }
    }



package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
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

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SeleniumStepTests
    {
    @Test
    public void openBrowser()
        {
        TestExecutionContext context = runTestWithSteps(createOpenBrowserStep());
        MuseMockDriver driver = (MuseMockDriver) context.getVariable(BrowserStepExecutionContext.DEFAULT_DRIVER_VARIABLE_NAME);
        Assert.assertNotNull(driver);
        }

    @Test
    public void openAndCloseBrowser()
        {
        TestExecutionContext context = runTestWithSteps(createOpenBrowserStep(), new StepConfiguration(CloseBrowser.TYPE_ID));
        MuseMockDriver driver = (MuseMockDriver) context.getVariable(BrowserStepExecutionContext.DEFAULT_DRIVER_VARIABLE_NAME);
        Assert.assertNull(driver);
        }

    @Test
    public void gotoUrl()
        {
        final String URL = "thetesturl";
        StepConfiguration goto_url = new StepConfiguration(GotoUrl.TYPE_ID);
        goto_url.addSource(GotoUrl.URL_PARAM, ValueSourceConfiguration.forValue(URL));
        TestExecutionContext context = runTestWithSteps(createOpenBrowserStep(), goto_url);
        MuseMockDriver driver = (MuseMockDriver) context.getVariable(BrowserStepExecutionContext.DEFAULT_DRIVER_VARIABLE_NAME);
        Assert.assertEquals(URL, driver.getCurrentUrl());
        }

    @Test
    public void clickElement() throws StepExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String id = "element#1";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addIdElement(id, element1);

        StepExecutionContext context = SimpleStepExecutionContext.create();
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration click = new StepConfiguration(ClickElement.TYPE_ID);
        click.addSource(ClickElement.ELEMENT_PARAM, ValueSourceConfiguration.forSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id)));
        MuseStep step = click.createStep(null);
        step.execute(context);

        Assert.assertTrue(element1.isClicked());
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
        String page_element_path = page1.getMetadata().getId() + "." + page_element_id;
        project.addResource(page1);

        MuseMockDriver driver = new MuseMockDriver();
        final MuseMockElement mock_element1 = new MuseMockElement();
        driver.addIdElement(element_id, mock_element1);

        StepExecutionContext context = SimpleStepExecutionContext.create(project);
        BrowserStepExecutionContext.putDriver(driver, context);

        StepConfiguration click = new StepConfiguration(ClickElement.TYPE_ID);
        click.addSource(ClickElement.ELEMENT_PARAM, ValueSourceConfiguration.forSource(PagesElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(page_element_path)));
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

    public static MuseProject createSeleniumTestProject()
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

    public static StepConfiguration createOpenBrowserStep()
        {
        StepConfiguration open_browser_step = new StepConfiguration(OpenBrowser.TYPE_ID);
        ValueSourceConfiguration provider_source = ValueSourceConfiguration.forTypeWithSource(ProjectResourceValueSource.TYPE_ID, ValueSourceConfiguration.forValue("driver-providers"));
        open_browser_step.addSource(OpenBrowser.PROVIDER_PARAM, provider_source);
        ValueSourceConfiguration browser_source = ValueSourceConfiguration.forTypeWithSource(ProjectResourceValueSource.TYPE_ID, ValueSourceConfiguration.forValue("capabilities-musemock"));
        open_browser_step.addSource(OpenBrowser.BROWSER_PARAM, browser_source);
        return open_browser_step;
        }
    }



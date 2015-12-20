package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.mocks.*;
import org.musetest.selenium.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SeleniumValueSourceTests
    {
    @Test
    public void currentUrl() throws MuseInstantiationException, ValueSourceResolutionError
        {
        final String THE_URL = "http://the.url/is/this";
        MuseMockDriver driver = new MuseMockDriver();
        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        driver.get(THE_URL);
        CurrentUrlValueSource source = new CurrentUrlValueSource(null, null);
        Assert.assertEquals(THE_URL, source.resolveValue(context));
        }

    @Test
    public void pageTitle() throws MuseInstantiationException, ValueSourceResolutionError
        {
        final String THE_URL = "http://the.url/is/this";
        MuseMockDriver driver = new MuseMockDriver();
        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        driver.get(THE_URL);
        PageTitleValueSource source = new PageTitleValueSource(null, null);
        String value = source.resolveValue(context).toString();
        Assert.assertTrue(value.startsWith("MuseMock") && value.endsWith("title"));
        }

    @Test
    public void elementByXpath() throws StepConfigurationError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String xpath = "//abc123";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addXpathElement(xpath, element1);

        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(XPathElementValueSource.TYPE_ID, xpath).createSource(null);
        Assert.assertTrue(source instanceof XPathElementValueSource);

        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        Object value = source.resolveValue(context);
        Assert.assertEquals(element1, value);
        }

    @Test
    public void elementById() throws StepConfigurationError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String id = "element#1";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addIdElement(id, element1);

        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(IdElementValueSource.TYPE_ID, id).createSource(null);
        Assert.assertTrue(source instanceof IdElementValueSource);

        StepExecutionContext context = new DummyStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        Object value = source.resolveValue(context);
        Assert.assertEquals(element1, value);
        }
    }



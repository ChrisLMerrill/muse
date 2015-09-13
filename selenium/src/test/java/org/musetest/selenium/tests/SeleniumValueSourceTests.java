package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.mocks.*;
import org.musetest.selenium.values.*;

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
        StepExecutionContext context = SimpleStepExecutionContext.create();
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
        StepExecutionContext context = SimpleStepExecutionContext.create();
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

        StepExecutionContext context = SimpleStepExecutionContext.create();
        BrowserStepExecutionContext.putDriver(driver, context);
        Object value = source.resolveValue(context);
        Assert.assertEquals(element1, value);
        }

    @Test
    public void elementByXpathQuickEditSupport() throws ValueSourceResolutionError, MuseInstantiationException
        {
        ValueSourceConfiguration config = ValueSourceQuickEditSupporters.parseWithAll("<xpath:\"//123/123/123\">", TEST_PROJECT).get(0);
        Assert.assertEquals(XPathElementValueSource.TYPE_ID, config.getType());
        Assert.assertEquals("//123/123/123", config.getSource().getValue());
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

        StepExecutionContext context = SimpleStepExecutionContext.create();
        BrowserStepExecutionContext.putDriver(driver, context);
        Object value = source.resolveValue(context);
        Assert.assertEquals(element1, value);
        }

    @Test
    public void elementByIdQuickEditSupport() throws ValueSourceResolutionError, MuseInstantiationException
        {
        ValueSourceConfiguration config = ValueSourceQuickEditSupporters.parseWithAll("<id:\"abc\">", TEST_PROJECT).get(0);
        Assert.assertEquals(IdElementValueSource.TYPE_ID, config.getType());
        Assert.assertEquals("abc", config.getSource().getValue());
        }

    static MuseProject TEST_PROJECT = new SimpleProject(new InMemoryResourceStore());
    }



package org.musetest.selenium.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.mocks.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.selenium.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.mocks.*;
import org.musetest.selenium.values.*;
import org.openqa.selenium.*;

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
        StepExecutionContext context = new MockStepExecutionContext();
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
        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        driver.get(THE_URL);
        PageTitleValueSource source = new PageTitleValueSource(null, null);
        String value = source.resolveValue(context).toString();
        Assert.assertTrue(value.startsWith("MuseMock") && value.endsWith("title"));
        }

    @Test
    public void elementByXpath() throws MuseExecutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        final String xpath = "//abc123";
        final MuseMockElement element1 = new MuseMockElement();
        driver.addXpathElement(xpath, element1);

        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(XPathElementValueSource.TYPE_ID, xpath).createSource();
        Assert.assertTrue(source instanceof XPathElementValueSource);

        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        Object value = source.resolveValue(context);
        Assert.assertEquals(element1, value);
        }

    @Test
    public void elementById() throws MuseExecutionError
        {
        final String id = "element#1";
        final MuseMockElement element1 = new MuseMockElement();

        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(IdElementValueSource.TYPE_ID, id).createSource();
        Assert.assertTrue(source instanceof IdElementValueSource);

        Object value = resolveSource(element1, id, source);
        Assert.assertEquals(element1, value);
        }

    @Test
    public void elementVisible() throws MuseExecutionError
        {
        final String id = "visible";
        final MuseMockElement element1 = new MuseMockElement();
        element1.setDisplayed(true);

        ValueSourceConfiguration locator = ValueSourceConfiguration.forTypeWithSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id));
        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(ElementVisibleCondition.TYPE_ID, locator).createSource();
        Assert.assertTrue(source instanceof ElementVisibleCondition);

        Object value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.TRUE, value);

        element1.setDisplayed(false);
        value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.FALSE, value);
        }

    @Test
    public void elementExists() throws MuseExecutionError
        {
        final String id = "exists";
        final MuseMockElement element1 = new MuseMockElement();

        ValueSourceConfiguration locator = ValueSourceConfiguration.forTypeWithSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id));
        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(ElementExistsCondition.TYPE_ID, locator).createSource();
        Assert.assertTrue(source instanceof ElementExistsCondition);

        Object value = resolveSource(null, null, source);
        Assert.assertEquals(Boolean.FALSE, value);

        value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.TRUE, value);
        }

    @Test
    public void elementEnabled() throws MuseExecutionError
        {
        final String id = "enabled";
        final MuseMockElement element1 = new MuseMockElement();
        element1.setEnabled(true);

        ValueSourceConfiguration locator = ValueSourceConfiguration.forTypeWithSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id));
        MuseValueSource source  = ValueSourceConfiguration.forTypeWithSource(ElementEnabledCondition.TYPE_ID, locator).createSource();
        Assert.assertTrue(source instanceof ElementEnabledCondition);

        Object value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.TRUE, value);

        element1.setEnabled(false);
        value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.FALSE, value);
        }

    @Test
    public void elementSelected() throws MuseExecutionError
        {
        final String id = "selected";
        final MuseMockElement element1 = new MuseMockElement();
        element1.setSelected(true);

        ValueSourceConfiguration locator = ValueSourceConfiguration.forTypeWithSource(IdElementValueSource.TYPE_ID, ValueSourceConfiguration.forValue(id));
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(ElementSelectedCondition.TYPE_ID, locator).createSource();
        Assert.assertTrue(source instanceof ElementSelectedCondition);

        Object value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.TRUE, value);

        element1.setSelected(false);
        value = resolveSource(element1, id, source);
        Assert.assertEquals(Boolean.FALSE, value);
        }

    private Object resolveSource(WebElement element, String element_id, MuseValueSource source) throws ValueSourceResolutionError
        {
        MuseMockDriver driver = new MuseMockDriver();
        if (element != null)
            driver.addIdElement(element_id, element);
        StepExecutionContext context = new MockStepExecutionContext();
        BrowserStepExecutionContext.putDriver(driver, context);
        return source.resolveValue(context);
        }
    }



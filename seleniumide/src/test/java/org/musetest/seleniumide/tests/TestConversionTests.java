package org.musetest.seleniumide.tests;

import org.junit.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestConversionTests
    {
    @Test
    public void convertExample() throws IOException, UnsupportedError
        {
        TestConverter converter = new TestConverter(getClass().getResourceAsStream("/example.html"));
        SteppedTest test = converter.convert()._test;

        List<StepConfiguration> steps = test.getStep().getChildren();

        Assert.assertEquals(OpenBrowser.TYPE_ID, steps.get(0).getType());

        Assert.assertEquals(GotoUrl.TYPE_ID, steps.get(1).getType());
        Assert.assertEquals("http://www.example.com/login/", steps.get(1).getSource(GotoUrl.URL_PARAM).getValue());

        Assert.assertEquals(SendKeys.TYPE_ID, steps.get(2).getType());
        Assert.assertEquals("user@example.com", steps.get(2).getSource(SendKeys.KEYS_PARAM).getValue());
        Assert.assertEquals(NameElementValueSource.TYPE_ID, steps.get(2).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assert.assertEquals("username", steps.get(2).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());

        Assert.assertEquals(SendKeys.TYPE_ID, steps.get(3).getType());
        Assert.assertEquals("mypass", steps.get(3).getSource(SendKeys.KEYS_PARAM).getValue());
        Assert.assertEquals(IdElementValueSource.TYPE_ID, steps.get(3).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assert.assertEquals("password", steps.get(3).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());

        Assert.assertEquals(ClickElement.TYPE_ID, steps.get(4).getType());
        Assert.assertEquals(XPathElementValueSource.TYPE_ID, steps.get(4).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assert.assertEquals("//div[@id='login_panel']/input[3]", steps.get(4).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());

        Assert.assertEquals(ClickElement.TYPE_ID, steps.get(5).getType());
        Assert.assertEquals(CssElementValueSource.TYPE_ID, steps.get(5).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assert.assertEquals("a.login > span", steps.get(5).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());
        }
    }



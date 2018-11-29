package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
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
class TestConversionTests
    {
    @Test
    void convertExample() throws IOException, UnsupportedError
        {
        TestConverter converter = new TestConverter(getClass().getResourceAsStream("/example.html"));
        SteppedTest test = converter.convert()._test;

        List<StepConfiguration> steps = test.getStep().getChildren();

        Assertions.assertEquals(OpenBrowser.TYPE_ID, steps.get(0).getType());

        Assertions.assertEquals(GotoUrl.TYPE_ID, steps.get(1).getType());
        Assertions.assertEquals("http://www.example.com/login/", steps.get(1).getSource(GotoUrl.URL_PARAM).getValue());

        Assertions.assertEquals(SendKeys.TYPE_ID, steps.get(2).getType());
        Assertions.assertEquals("user@example.com", steps.get(2).getSource(SendKeys.KEYS_PARAM).getValue());
        Assertions.assertEquals(NameElementValueSource.TYPE_ID, steps.get(2).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assertions.assertEquals("username", steps.get(2).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());

        Assertions.assertEquals(SendKeys.TYPE_ID, steps.get(3).getType());
        Assertions.assertEquals("mypass", steps.get(3).getSource(SendKeys.KEYS_PARAM).getValue());
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, steps.get(3).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assertions.assertEquals("password", steps.get(3).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());

        Assertions.assertEquals(ClickElement.TYPE_ID, steps.get(4).getType());
        Assertions.assertEquals(XPathElementValueSource.TYPE_ID, steps.get(4).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assertions.assertEquals("//div[@id='login_panel']/input[3]", steps.get(4).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());

        Assertions.assertEquals(ClickElement.TYPE_ID, steps.get(5).getType());
        Assertions.assertEquals(CssElementValueSource.TYPE_ID, steps.get(5).getSource(SendKeys.ELEMENT_PARAM).getType());
        Assertions.assertEquals("a.login > span", steps.get(5).getSource(SendKeys.ELEMENT_PARAM).getSource().getValue());
        }
    }



package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.steps.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class CommandConversionTests
    {
    @Test
    void unsupportedCommand()
        {
        try
            {
            StepConverters.get().convertStep("", "unsupported-command", "param1", "param2");
            Assertions.fail("exception should be thrown");
            }
        catch (UnsupportedError e)
            {
            // OK
            }
        }

    @Test
    void storeVariable() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", StoreValueConverter.STORE, "var1", "value2");
        Assertions.assertEquals(StoreVariable.TYPE_ID, step.getType(), "converted step does not have the correct type");
        Assertions.assertEquals("var1", step.getSource(StoreVariable.NAME_PARAM).getValue(), "the variable name is not set correctly");
        Assertions.assertEquals("value2", step.getSource(StoreVariable.VALUE_PARAM).getValue(), "the value is not set correctly");
        }

    @Test
    void click() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", ClickConverter.CLICK, "id=it", "");
        Assertions.assertEquals(ClickElement.TYPE_ID, step.getType(), "converted step does not have the correct type");
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(ClickElement.ELEMENT_PARAM).getType());
        Assertions.assertEquals("it", step.getSource(ClickElement.ELEMENT_PARAM).getSource().getValue());
        }

    @Test
    void submit() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", SubmitConverter.SUBMIT, "id=the-form", "");
        Assertions.assertEquals(SubmitElement.TYPE_ID, step.getType(), "converted step does not have the correct type");
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SubmitElement.ELEMENT_PARAM).getType());
        Assertions.assertEquals("the-form", step.getSource(SubmitElement.ELEMENT_PARAM).getSource().getValue());
        }

    @Test
    void setWindowSize() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", SetWindowSizeConverter.COMMAND, "200x100", "");
        Assertions.assertEquals(SetBrowserSize.TYPE_ID, step.getType(), "converted step does not have the correct type");
        Assertions.assertEquals(200L, step.getSource(SetBrowserSize.WIDTH_PARAM).getValue());
        Assertions.assertEquals(100L, step.getSource(SetBrowserSize.HEIGHT_PARAM).getValue());
        }

    @Test
    void selectByLabel() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", SelectConverter.SELECT, "id=InputMonth", "label=04");
        Assertions.assertEquals(SelectOptionByText.TYPE_ID, step.getType());
        Assertions.assertEquals("04", step.getSource(SelectOptionByText.TEXT_PARAM).getValue());
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SelectOptionByText.ELEMENT_PARAM).getType());
        Assertions.assertEquals("InputMonth", step.getSource(SelectOptionByText.ELEMENT_PARAM).getSource().getValue());
        }

    @Test
    void selectByIndex() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", SelectConverter.SELECT, "id=InputDay", "index=2");
        Assertions.assertEquals(SelectOptionByIndex.TYPE_ID, step.getType());
        Assertions.assertEquals("2", step.getSource(SelectOptionByIndex.INDEX_PARAM).getValue());
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SelectOptionByIndex.ELEMENT_PARAM).getType());
        Assertions.assertEquals("InputDay", step.getSource(SelectOptionByIndex.ELEMENT_PARAM).getSource().getValue());
        }

    @Test
    void echo() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", EchoConverter.ECHO, "a message", "");
        Assertions.assertEquals(LogMessage.TYPE_ID, step.getType());
        Assertions.assertEquals("a message", step.getSource(LogMessage.MESSAGE_PARAM).getValue());
        }
    }
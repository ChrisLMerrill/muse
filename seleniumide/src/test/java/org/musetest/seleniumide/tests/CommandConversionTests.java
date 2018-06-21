package org.musetest.seleniumide.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.steps.*;
import org.musetest.seleniumide.tests.mocks.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CommandConversionTests
    {
    @Test
    public void storeVariable() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", "store", "var1", "value2");
        Assert.assertEquals("converted step does not have the correct type", StoreVariable.TYPE_ID, step.getType());
        Assert.assertEquals("the variable name is not set correctly", "var1", step.getSource(StoreVariable.NAME_PARAM).getValue());
        Assert.assertEquals("the value is not set correctly", "value2", step.getSource(StoreVariable.VALUE_PARAM).getValue());
        }

    @Test
    public void selectByLabel() throws IOException, UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", "select", "id=InputMonth", "label=04");
        Assert.assertEquals(SelectOptionByText.TYPE_ID, step.getType());
        Assert.assertEquals("04", step.getSource(SelectOptionByText.TEXT_PARAM).getValue());
        Assert.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SelectOptionByText.ELEMENT_PARAM).getType());
        Assert.assertEquals("InputMonth", step.getSource(SelectOptionByText.ELEMENT_PARAM).getSource().getValue());
        }

    @Test
    public void selectByIndex() throws IOException, UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", "select", "id=InputDay", "index=2");
        Assert.assertEquals(SelectOptionByIndex.TYPE_ID, step.getType());
        Assert.assertEquals("2", step.getSource(SelectOptionByIndex.INDEX_PARAM).getValue());
        Assert.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SelectOptionByIndex.ELEMENT_PARAM).getType());
        Assert.assertEquals("InputDay", step.getSource(SelectOptionByIndex.ELEMENT_PARAM).getSource().getValue());
        }
    }



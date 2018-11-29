package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.steps.*;

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
        Assertions.assertEquals(StoreVariable.TYPE_ID, step.getType(), "converted step does not have the correct type");
        Assertions.assertEquals("var1", step.getSource(StoreVariable.NAME_PARAM).getValue(), "the variable name is not set correctly");
        Assertions.assertEquals("value2", step.getSource(StoreVariable.VALUE_PARAM).getValue(), "the value is not set correctly");
        }

    @Test
    public void selectByLabel() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", "select", "id=InputMonth", "label=04");
        Assertions.assertEquals(SelectOptionByText.TYPE_ID, step.getType());
        Assertions.assertEquals("04", step.getSource(SelectOptionByText.TEXT_PARAM).getValue());
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SelectOptionByText.ELEMENT_PARAM).getType());
        Assertions.assertEquals("InputMonth", step.getSource(SelectOptionByText.ELEMENT_PARAM).getSource().getValue());
        }

    @Test
    public void selectByIndex() throws UnsupportedError
        {
        StepConfiguration step = StepConverters.get().convertStep("", "select", "id=InputDay", "index=2");
        Assertions.assertEquals(SelectOptionByIndex.TYPE_ID, step.getType());
        Assertions.assertEquals("2", step.getSource(SelectOptionByIndex.INDEX_PARAM).getValue());
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, step.getSource(SelectOptionByIndex.ELEMENT_PARAM).getType());
        Assertions.assertEquals("InputDay", step.getSource(SelectOptionByIndex.ELEMENT_PARAM).getSource().getValue());
        }
    }



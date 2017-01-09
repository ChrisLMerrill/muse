package org.musetest.seleniumide.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.steps.*;
import org.musetest.seleniumide.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class VerifyAssertConversionTests
    {
    @Test
    public void convertExample() throws IOException, UnsupportedError
        {
        TestConverter converter = new TestConverter(getClass().getResourceAsStream("/verify.html"));
        SteppedTest test = converter.convert()._test;

        // check the verify step
        StepConfiguration verify_step = test.getStep().getChildren().get(4);
        Assert.assertEquals(Verify.TYPE_ID, verify_step.getType());
        Assert.assertNull(verify_step.getSource(Verify.TERMINATE_PARAM));

        ValueSourceConfiguration exists_condition = verify_step.getSource(Verify.CONDITION_PARAM);
        Assert.assertEquals(ElementExistsCondition.TYPE_ID, exists_condition.getType());

        ValueSourceConfiguration locator = exists_condition.getSource();
        Assert.assertEquals(LinkTextElementValueSource.TYPE_ID, locator.getType());
        Assert.assertEquals("ChrisLMerrill/muse", locator.getSource().getValue());

        // now check the assert step has the terminate param
        StepConfiguration assert_step = test.getStep().getChildren().get(5);
        Assert.assertEquals(Boolean.TRUE, assert_step.getSource(Verify.TERMINATE_PARAM).getValue());
        }
    }



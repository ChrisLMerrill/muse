package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.steptest.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class VerifyAssertConversionTests
    {
    @Test
    void verifyAndAssertElementExists() throws IOException, UnsupportedError
        {
        TestConverter converter = new TestConverter(getClass().getResourceAsStream("/verify.html"));
        SteppedTest test = converter.convert()._test;

        // check the verify step
        StepConfiguration verify_step = test.getStep().getChildren().get(4);
        Assertions.assertEquals(Verify.TYPE_ID, verify_step.getType());
        Assertions.assertNull(verify_step.getSource(Verify.TERMINATE_PARAM));

        ValueSourceConfiguration exists_condition = verify_step.getSource(Verify.CONDITION_PARAM);
        Assertions.assertEquals(ElementExistsCondition.TYPE_ID, exists_condition.getType());

        ValueSourceConfiguration locator = exists_condition.getSource();
        Assertions.assertEquals(LinkTextElementValueSource.TYPE_ID, locator.getType());
        Assertions.assertEquals("ChrisLMerrill/muse", locator.getSource().getValue());

        // now check the assert step has the terminate param
        StepConfiguration assert_step = test.getStep().getChildren().get(5);
        Assertions.assertEquals(Boolean.TRUE, assert_step.getSource(Verify.TERMINATE_PARAM).getValue());
        }

    @Test
    void elementTextEquality() throws IOException, UnsupportedError
        {
        TestConverter converter = new TestConverter(getClass().getResourceAsStream("/verify2.html"));
        SteppedTest test = converter.convert()._test;

        // check the verify step
        StepConfiguration verify_step = test.getStep().getChildren().get(1);
        Assertions.assertEquals(Verify.TYPE_ID, verify_step.getType());

        ValueSourceConfiguration equality = verify_step.getSource(Verify.CONDITION_PARAM);
        Assertions.assertEquals(EqualityCondition.TYPE_ID, equality.getType());

        ValueSourceConfiguration element_text = equality.getSource(EqualityCondition.LEFT_PARAM);
        Assertions.assertEquals(ElementText.TYPE_ID, element_text.getType());

        ValueSourceConfiguration locator = element_text.getSource();
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, locator.getType());
        Assertions.assertEquals("123", locator.getSource().getValue());

        ValueSourceConfiguration match_text = equality.getSource(EqualityCondition.RIGHT_PARAM);
        Assertions.assertEquals("TargetText", match_text.getValue());
        }
    }



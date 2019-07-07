package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;
import org.musetest.selenium.conditions.*;
import org.musetest.selenium.locators.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.conditions.*;
import org.musetest.seleniumide.steps.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ConditionConversionTests
    {
    @Test
    void exactMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "abc");
        Assertions.assertEquals(EqualityCondition.TYPE_ID, condition.getType());
        Assertions.assertEquals(ElementTextSource.TYPE_ID, condition.getSource(EqualityCondition.LEFT_PARAM).getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assertions.assertEquals("abc", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    @Test
    void inferredGlobMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "a?c");
        Assertions.assertEquals(GlobMatchCondition.TYPE_ID, condition.getType());
        Assertions.assertEquals("a?c", condition.getSource(GlobMatchCondition.PATTERN_PARAM).getValue());
        }

    @Test
    void explicitGlobMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "glob:a*c");
        Assertions.assertEquals(GlobMatchCondition.TYPE_ID, condition.getType());
        Assertions.assertEquals("a*c", condition.getSource(GlobMatchCondition.PATTERN_PARAM).getValue());
        }

    @Test
    void regexpMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "regexp:a.*c");
        Assertions.assertEquals(RegexMatchCondition.TYPE_ID, condition.getType());
        Assertions.assertEquals("a.*c", condition.getSource(RegexMatchCondition.PATTERN_PARAM).getValue());
        }

    @Test
    void assertTitle() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Title");
        ValueSourceConfiguration condition = converter.createConditionSource("the title", "");
        Assertions.assertEquals(EqualityCondition.TYPE_ID, condition.getType());
        Assertions.assertEquals(PageTitleValueSource.TYPE_ID, condition.getSource(EqualityCondition.LEFT_PARAM).getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assertions.assertEquals("the title", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    @Test
    void assertText() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=title", "target text");
        Assertions.assertEquals(EqualityCondition.TYPE_ID, condition.getType());
        
        ValueSourceConfiguration element_text = condition.getSource(EqualityCondition.LEFT_PARAM);
        Assertions.assertEquals(ElementTextSource.TYPE_ID, element_text.getType());
        ValueSourceConfiguration element = element_text.getSource();
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, element.getType());
        Assertions.assertEquals("title", element.getSource().getValue());

        Assertions.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assertions.assertEquals("target text", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    @Test
    void assertValue() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Value");
        ValueSourceConfiguration condition = converter.createConditionSource("id=title", "1st value");
        Assertions.assertEquals(EqualityCondition.TYPE_ID, condition.getType());

        ValueSourceConfiguration element_text = condition.getSource(EqualityCondition.LEFT_PARAM);
        Assertions.assertEquals(ElementValue.TYPE_ID, element_text.getType());
        ValueSourceConfiguration element = element_text.getSource();
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, element.getType());
        Assertions.assertEquals("title", element.getSource().getValue());

        Assertions.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assertions.assertEquals("1st value", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    @Test
    void assertSelectedValue() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("SelectedValue");
        ValueSourceConfiguration condition = converter.createConditionSource("id=title", "1st value");
        Assertions.assertEquals(EqualityCondition.TYPE_ID, condition.getType());

        ValueSourceConfiguration element_text = condition.getSource(EqualityCondition.LEFT_PARAM);
        Assertions.assertEquals(ElementValue.TYPE_ID, element_text.getType());
        ValueSourceConfiguration element = element_text.getSource();
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, element.getType());
        Assertions.assertEquals("title", element.getSource().getValue());

        Assertions.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assertions.assertEquals("1st value", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    @Test
    void assertSelectedLabel() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("SelectedLabel");
        ValueSourceConfiguration condition = converter.createConditionSource("id=title", "1st value");
        Assertions.assertEquals(EqualityCondition.TYPE_ID, condition.getType());

        ValueSourceConfiguration element_text = condition.getSource(EqualityCondition.LEFT_PARAM);
        Assertions.assertEquals(SelectedLabel.TYPE_ID, element_text.getType());
        ValueSourceConfiguration element = element_text.getSource();
        Assertions.assertEquals(IdElementValueSource.TYPE_ID, element.getType());
        Assertions.assertEquals("title", element.getSource().getValue());

        Assertions.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assertions.assertEquals("1st value", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    }



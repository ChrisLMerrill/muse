package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.conditions.*;

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
        Assertions.assertEquals(ElementText.TYPE_ID, condition.getSource(EqualityCondition.LEFT_PARAM).getType());
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
    }



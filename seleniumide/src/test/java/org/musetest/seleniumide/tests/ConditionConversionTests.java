package org.musetest.seleniumide.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.*;
import org.musetest.seleniumide.conditions.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ConditionConversionTests
    {
    @Test
    public void exactMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "abc");
        Assert.assertEquals(EqualityCondition.TYPE_ID, condition.getType());
        Assert.assertEquals(ElementText.TYPE_ID, condition.getSource(EqualityCondition.LEFT_PARAM).getType());
        Assert.assertEquals(StringValueSource.TYPE_ID, condition.getSource(EqualityCondition.RIGHT_PARAM).getType());
        Assert.assertEquals("abc", condition.getSource(EqualityCondition.RIGHT_PARAM).getValue());
        }

    @Test
    public void inferredGlobMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "a?c");
        Assert.assertEquals(GlobMatchCondition.TYPE_ID, condition.getType());
        Assert.assertEquals("a?c", condition.getSource(GlobMatchCondition.PATTERN_PARAM).getValue());
        }

    @Test
    public void explicitGlobMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "glob:a*c");
        Assert.assertEquals(GlobMatchCondition.TYPE_ID, condition.getType());
        Assert.assertEquals("a*c", condition.getSource(GlobMatchCondition.PATTERN_PARAM).getValue());
        }

    @Test
    public void regexpMatch() throws UnsupportedError
        {
        ConditionConverter converter = ConditionConverters.getInstance().find("Text");
        ValueSourceConfiguration condition = converter.createConditionSource("id=123", "regexp:a.*c");
        Assert.assertEquals(RegexMatchCondition.TYPE_ID, condition.getType());
        Assert.assertEquals("a.*c", condition.getSource(RegexMatchCondition.PATTERN_PARAM).getValue());
        }
    }



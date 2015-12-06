package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceStringExpressionSupportTests
    {
    @Test
    public void emptyStringSource()
        {
        String s = "";
        testParseFromAndToString(new StringValueSourceStringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void stringSource()
        {
        String s = "abc";
        testParseFromAndToString(new StringValueSourceStringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void stringSourceWithEscapedQuotes()
        {
        String s = "string \\\" has \\\" quotes";
        testParseFromAndToString(new StringValueSourceStringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void stringSourceWithInvalidQuotes()
        {
        String s = "string \" quote";
        testParseFromAndToString(new StringValueSourceStringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void integerSource()
        {
        Long value = 123L;
        testParseFromAndToString(new IntegerValueSourceStringExpressionSupport(), value.toString(), IntegerValueSource.TYPE_ID, value);
        }

    @Test
    public void booleanSourceTrue()
        {
        testParseFromAndToString(new BooleanValueSourceStringExpressionSupport(), Boolean.TRUE.toString(), BooleanValueSource.TYPE_ID, true);
        }

    @Test
    public void booleanSourceFalse()
        {
        testParseFromAndToString(new BooleanValueSourceStringExpressionSupport(), Boolean.FALSE.toString(), BooleanValueSource.TYPE_ID, false);
        }

    @Test
    public void nullSource()
        {
        testParseFromAndToString(new NullValueSourceStringExpressionSupport(), "null", NullValueSource.TYPE_ID, null);
        }

    private void testParseFromAndToString(ValueSourceStringExpressionSupport support, String string_content, String type, Object content)
        {
        ValueSourceConfiguration parsed = support.fromLiteral(string_content, null);
        Assert.assertEquals(type, parsed.getType());
        Assert.assertEquals(content, parsed.getValue());

        String stringified = support.toString(parsed, null);
        Assert.assertEquals(string_content, stringified);
        }

    @Test
    public void stringConcatenationSource()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("\"abc=\" + 123", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration source = config_list.get(0);
        Assert.assertNotNull(source);
        Assert.assertEquals("concatenate", source.getType());

        Assert.assertNotNull(source.getSourceList());
        Assert.assertEquals(2, source.getSourceList().size());

        Assert.assertEquals("abc=", source.getSourceList().get(0).getValue());
        Assert.assertEquals(123L, source.getSourceList().get(1).getValue());
        }

    @Test
    public void toFromStringConcatenation()
        {
        ValueSourceConfiguration source = new ValueSourceConfiguration();
        source.setType("concatenate");
        source.addSource(ValueSourceConfiguration.forValue("abc"));
        source.addSource(ValueSourceConfiguration.forValue(123L));
        stringifyAndParse(source);
        }

    @Test
    public void variableSource()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("$\"var1\"", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("variable", config.getType());
        Assert.assertNotNull(config.getSource());
        Assert.assertEquals("string", config.getSource().getType());
        Assert.assertEquals("var1", config.getSource().getValue());
        }

    @Test
    public void toFromVariable()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("variable");

        ValueSourceConfiguration source_config = new ValueSourceConfiguration();
        source_config.setType("string");
        source_config.setValue("var1");
        config.setSource(source_config);
        stringifyAndParse(config);
        }

    @Test
    public void equalsCondition()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("\"abc\" = \"123\"", TEST_PROJECT);
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("equals", config.getType());

        ValueSourceConfiguration left = config.getSourceMap().get("left");
        Assert.assertNotNull(left);
        Assert.assertEquals("string", left.getType());
        Assert.assertEquals("abc", left.getValue());

        ValueSourceConfiguration right = config.getSourceMap().get("right");
        Assert.assertNotNull(right);
        Assert.assertEquals("string", right.getType());
        Assert.assertEquals("123", right.getValue());
        }

    @Test
    public void toFromEquals()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("equals");
        config.addSource("left", ValueSourceConfiguration.forValue(2L));
        config.addSource("right", ValueSourceConfiguration.forValue(1L));

        stringifyAndParse(config);
        }

    @Test
    public void greaterCondition()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("12 > 10", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("greaterthan", config.getType());

        ValueSourceConfiguration left = config.getSourceMap().get("left");
        Assert.assertNotNull(left);
        Assert.assertEquals("integer", left.getType());
        Assert.assertEquals(12L, left.getValue());

        ValueSourceConfiguration right = config.getSourceMap().get("right");
        Assert.assertNotNull(right);
        Assert.assertEquals("integer", right.getType());
        Assert.assertEquals(10L, right.getValue());
        }

    @Test
    public void toFromGreater()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("greaterthan");
        config.addSource("left", ValueSourceConfiguration.forValue(2L));
        config.addSource("right", ValueSourceConfiguration.forValue(1L));

        stringifyAndParse(config);
        }

    @Test
    public void lesserCondition()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("12 < 10", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("lessthan", config.getType());

        ValueSourceConfiguration left = config.getSourceMap().get("left");
        Assert.assertNotNull(left);
        Assert.assertEquals("integer", left.getType());
        Assert.assertEquals(12L, left.getValue());

        ValueSourceConfiguration right = config.getSourceMap().get("right");
        Assert.assertNotNull(right);
        Assert.assertEquals("integer", right.getType());
        Assert.assertEquals(10L, right.getValue());
        }

    @Test
    public void toFromLesser()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("lessthan");
        config.addSource("left", ValueSourceConfiguration.forValue(2L));
        config.addSource("right", ValueSourceConfiguration.forValue(1L));

        stringifyAndParse(config);
        }

    private void stringifyAndParse(ValueSourceConfiguration source)
        {
        List<String> stringified = ValueSourceQuickEditSupporters.asStringFromAll(source, TEST_PROJECT);
        Assert.assertEquals(1, stringified.size());
        List<ValueSourceConfiguration> parsed = ValueSourceQuickEditSupporters.parseWithAll(stringified.get(0), TEST_PROJECT);
        Assert.assertEquals(1, parsed.size());
        Assert.assertEquals(parsed.get(0), source);
        }

    static MuseProject TEST_PROJECT = new SimpleProject(new InMemoryResourceStore());
    }



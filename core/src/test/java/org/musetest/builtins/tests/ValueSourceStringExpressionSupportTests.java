package org.musetest.builtins.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.builtins.value.collection.*;
import org.musetest.builtins.value.logic.*;
import org.musetest.builtins.value.property.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.values.*;
import org.musetest.core.values.strings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ValueSourceStringExpressionSupportTests
    {
    @Test
    void emptyStringSource()
        {
        String s = "";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    void stringSource()
        {
        String s = "abc";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    void stringSourceWithEscapedQuotes()
        {
        String s = "string \\\" has \\\" quotes";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    void stringSourceWithInvalidQuotes()
        {
        String s = "string \" quote";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    void integerSource()
        {
        Long value = 123L;
        testParseFromAndToString(new IntegerValueSource.StringExpressionSupport(), value.toString(), IntegerValueSource.TYPE_ID, value);
        }

    @Test
    void booleanSourceTrue()
        {
        testParseFromAndToString(new BooleanValueSource.StringExpressionSupport(), Boolean.TRUE.toString(), BooleanValueSource.TYPE_ID, true);
        }

    @Test
    void booleanSourceFalse()
        {
        testParseFromAndToString(new BooleanValueSource.StringExpressionSupport(), Boolean.FALSE.toString(), BooleanValueSource.TYPE_ID, false);
        }

    @Test
    void nullSource()
        {
        testParseFromAndToString(new NullValueSource.StringExpressionSupport(), "null", NullValueSource.TYPE_ID, null);
        }

    private void testParseFromAndToString(ValueSourceStringExpressionSupport support, String string_content, String type, Object content)
        {
        ValueSourceConfiguration parsed = support.fromLiteral(string_content, null);
        Assertions.assertEquals(type, parsed.getType());
        Assertions.assertEquals(content, parsed.getValue());

        String stringified = support.toString(parsed, new RootStringExpressionContext(new SimpleProject()));
        Assertions.assertEquals(string_content, stringified);
        }

    @Test
    void stringConcatenation2Sources()
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(123L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(456L);
        AdditionSource.StringExpressionSupport supporter = new AdditionSource.StringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(left, "+", right, TEST_PROJECT);

        Assertions.assertNotNull(config);
        Assertions.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assertions.assertEquals(left, config.getSourceList().get(0));
        Assertions.assertEquals(right, config.getSourceList().get(1));

        String stringified = supporter.toString(config, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals("123 + 456", stringified);
        }

    @Test
    void stringConcatenation3Sources()
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(123L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(456L);
        ValueSourceConfiguration far_right = ValueSourceConfiguration.forValue(789L);
        AdditionSource.StringExpressionSupport supporter = new AdditionSource.StringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(left, "+", right, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", far_right, TEST_PROJECT);

        Assertions.assertNotNull(config);
        Assertions.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assertions.assertEquals(left, config.getSourceList().get(0));
        Assertions.assertEquals(right, config.getSourceList().get(1));
        Assertions.assertEquals(far_right, config.getSourceList().get(2));

        String stringified = supporter.toString(config, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals("123 + 456 + 789", stringified);
        }

    @Test
    void stringConcatenation4Sources()
        {
        ValueSourceConfiguration far_left = ValueSourceConfiguration.forValue(11L);
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(22L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(33L);
        ValueSourceConfiguration far_right = ValueSourceConfiguration.forValue(44L);
        AdditionSource.StringExpressionSupport supporter = new AdditionSource.StringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(far_left, "+", left, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", right, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", far_right, TEST_PROJECT);

        Assertions.assertNotNull(config);
        Assertions.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assertions.assertEquals(far_left, config.getSourceList().get(0));
        Assertions.assertEquals(left, config.getSourceList().get(1));
        Assertions.assertEquals(right, config.getSourceList().get(2));
        Assertions.assertEquals(far_right, config.getSourceList().get(3));

        String stringified = supporter.toString(config, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals("11 + 22 + 33 + 44", stringified);
        }

    @Test
    void variableSource()
        {
        ValueSourceConfiguration string_source = ValueSourceConfiguration.forValue("var1");
        ValueSourceConfiguration config = new VariableValueSource.StringExpressionSupport().fromPrefixedExpression("$", string_source, TEST_PROJECT);
        Assertions.assertNotNull(config);

        Assertions.assertEquals(VariableValueSource.TYPE_ID, config.getType());
        Assertions.assertNotNull(config.getSource());
        Assertions.assertEquals(StringValueSource.TYPE_ID, config.getSource().getType());
        Assertions.assertEquals("var1", config.getSource().getValue());
        }

    @Test
    void projectResourceSource()
        {
        ValueSourceConfiguration string_source = ValueSourceConfiguration.forValue("res1");
        ValueSourceConfiguration config = new ProjectResourceValueSource.StringExpressionSupport().fromPrefixedExpression("#", string_source, TEST_PROJECT);
        Assertions.assertNotNull(config);

        Assertions.assertEquals(ProjectResourceValueSource.TYPE_ID, config.getType());
        Assertions.assertNotNull(config.getSource());
        Assertions.assertEquals(StringValueSource.TYPE_ID, config.getSource().getType());
        Assertions.assertEquals("res1", config.getSource().getValue());
        }

    @Test
    void equals()
        {
        testBinaryCondition(new EqualityCondition.StringExpressionSupport());
        }

    @Test
    void greaterThan()
        {
        testBinaryCondition(new GreaterThanCondition.StringExpressionSupport());
        }

    @Test
    void lessThan()
        {
        testBinaryCondition(new LessThanCondition.StringExpressionSupport());
        }

    private void testBinaryCondition(BinaryConditionStringExpressionSupport supporter)
        {
        String left = "abc";
        String right = "xyz";
        String to_parse = String.format("\"%s\" %s \"%s\"", left, supporter.getOperator(), right);
        ValueSourceConfiguration parsed = supporter.fromBinaryExpression(ValueSourceConfiguration.forValue(left), supporter.getOperator(), ValueSourceConfiguration.forValue(right), TEST_PROJECT);

        Assertions.assertEquals(supporter.getSourceType(), parsed.getType());
        Assertions.assertEquals(left, parsed.getSource(BinaryCondition.LEFT_PARAM).getValue());
        Assertions.assertEquals(right, parsed.getSource(BinaryCondition.RIGHT_PARAM).getValue());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals(to_parse, stringified);
        }

    @Test
    void dateFormat()
        {
        DateFormatValueSource.StringExpressionSupport supporter = new DateFormatValueSource.StringExpressionSupport();
        ValueSourceConfiguration date_config = ValueSourceConfiguration.forValue("date");
        ValueSourceConfiguration format_config = ValueSourceConfiguration.forValue("format");
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(date_config);
        arguments.add(format_config);

        ValueSourceConfiguration parsed = supporter.fromArgumentedExpression(supporter.getName(), arguments, TEST_PROJECT);

        Assertions.assertEquals(DateFormatValueSource.TYPE_ID, parsed.getType());
        Assertions.assertEquals(date_config, parsed.getSource(DateFormatValueSource.DATE_PARAM));
        Assertions.assertEquals(format_config, parsed.getSource(DateFormatValueSource.FORMAT_PARAM));

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals("formatDate(\"date\",\"format\")", stringified);
        }

    @Test
    void propertySource()
        {
        ValueSourceStringExpressionSupport supporter = new PropertySource.StringExpressionSupport();
        String left = "\"abc\"";
        String right = "\"xyz\"";
        String to_parse = String.format("\"%s\".\"%s\"", left, right);
        ValueSourceConfiguration parsed = supporter.fromDotExpression(ValueSourceConfiguration.forValue(left), ValueSourceConfiguration.forValue(right), TEST_PROJECT);

        Assertions.assertEquals(PropertySource.TYPE_ID, parsed.getType());
        Assertions.assertEquals(left, parsed.getSource(PropertySource.TARGET_PARAM).getValue());
        Assertions.assertEquals(right, parsed.getSource(PropertySource.NAME_PARAM).getValue());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals(to_parse, stringified);
        }

    @Test
    void listSource()
        {
        ValueSourceStringExpressionSupport supporter = new ListSource.StringExpressionSupport();
        String to_parse = "[11,22]";
        List<ValueSourceConfiguration> elements = new ArrayList<>();
        elements.add(ValueSourceConfiguration.forValue(11L));
        elements.add(ValueSourceConfiguration.forValue(22L));
        ValueSourceConfiguration parsed = supporter.fromArrayExpression(elements, TEST_PROJECT);

        Assertions.assertEquals(ListSource.TYPE_ID, parsed.getType());
        Assertions.assertEquals(elements.get(0).getValue(), parsed.getSource(0).getValue());
        Assertions.assertEquals(elements.get(1).getValue(), parsed.getSource(1).getValue());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals(to_parse, stringified);
        }

    private static MuseProject TEST_PROJECT = new SimpleProject(new InMemoryResourceStorage());
    }

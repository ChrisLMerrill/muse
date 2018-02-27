package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
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
public class ValueSourceStringExpressionSupportTests
    {
    @Test
    public void emptyStringSource()
        {
        String s = "";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void stringSource()
        {
        String s = "abc";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void stringSourceWithEscapedQuotes()
        {
        String s = "string \\\" has \\\" quotes";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void stringSourceWithInvalidQuotes()
        {
        String s = "string \" quote";
        testParseFromAndToString(new StringValueSource.StringExpressionSupport(), "\"" + s + "\"", StringValueSource.TYPE_ID, s);
        }

    @Test
    public void integerSource()
        {
        Long value = 123L;
        testParseFromAndToString(new IntegerValueSource.StringExpressionSupport(), value.toString(), IntegerValueSource.TYPE_ID, value);
        }

    @Test
    public void booleanSourceTrue()
        {
        testParseFromAndToString(new BooleanValueSource.StringExpressionSupport(), Boolean.TRUE.toString(), BooleanValueSource.TYPE_ID, true);
        }

    @Test
    public void booleanSourceFalse()
        {
        testParseFromAndToString(new BooleanValueSource.StringExpressionSupport(), Boolean.FALSE.toString(), BooleanValueSource.TYPE_ID, false);
        }

    @Test
    public void nullSource()
        {
        testParseFromAndToString(new NullValueSource.StringExpressionSupport(), "null", NullValueSource.TYPE_ID, null);
        }

    private void testParseFromAndToString(ValueSourceStringExpressionSupport support, String string_content, String type, Object content)
        {
        ValueSourceConfiguration parsed = support.fromLiteral(string_content, null);
        Assert.assertEquals(type, parsed.getType());
        Assert.assertEquals(content, parsed.getValue());

        String stringified = support.toString(parsed, new RootStringExpressionContext(new SimpleProject()));
        Assert.assertEquals(string_content, stringified);
        }

    @Test
    public void stringConcatenation2Sources()
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(123L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(456L);
        AdditionSource.StringExpressionSupport supporter = new AdditionSource.StringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(left, "+", right, TEST_PROJECT);

        Assert.assertNotNull(config);
        Assert.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assert.assertEquals(left, config.getSourceList().get(0));
        Assert.assertEquals(right, config.getSourceList().get(1));

        String stringified = supporter.toString(config, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals("123 + 456", stringified);
        }

    @Test
    public void stringConcatenation3Sources()
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(123L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(456L);
        ValueSourceConfiguration far_right = ValueSourceConfiguration.forValue(789L);
        AdditionSource.StringExpressionSupport supporter = new AdditionSource.StringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(left, "+", right, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", far_right, TEST_PROJECT);

        Assert.assertNotNull(config);
        Assert.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assert.assertEquals(left, config.getSourceList().get(0));
        Assert.assertEquals(right, config.getSourceList().get(1));
        Assert.assertEquals(far_right, config.getSourceList().get(2));

        String stringified = supporter.toString(config, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals("123 + 456 + 789", stringified);
        }

    @Test
    public void stringConcatenation4Sources()
        {
        ValueSourceConfiguration far_left = ValueSourceConfiguration.forValue(11L);
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(22L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(33L);
        ValueSourceConfiguration far_right = ValueSourceConfiguration.forValue(44L);
        AdditionSource.StringExpressionSupport supporter = new AdditionSource.StringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(far_left, "+", left, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", right, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", far_right, TEST_PROJECT);

        Assert.assertNotNull(config);
        Assert.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assert.assertEquals(far_left, config.getSourceList().get(0));
        Assert.assertEquals(left, config.getSourceList().get(1));
        Assert.assertEquals(right, config.getSourceList().get(2));
        Assert.assertEquals(far_right, config.getSourceList().get(3));

        String stringified = supporter.toString(config, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals("11 + 22 + 33 + 44", stringified);
        }

    @Test
    public void variableSource()
        {
        ValueSourceConfiguration string_source = ValueSourceConfiguration.forValue("var1");
        ValueSourceConfiguration config = new VariableValueSource.StringExpressionSupport().fromPrefixedExpression("$", string_source, TEST_PROJECT);
        Assert.assertNotNull(config);

        Assert.assertEquals(VariableValueSource.TYPE_ID, config.getType());
        Assert.assertNotNull(config.getSource());
        Assert.assertEquals(StringValueSource.TYPE_ID, config.getSource().getType());
        Assert.assertEquals("var1", config.getSource().getValue());
        }

    @Test
    public void projectResourceSource()
        {
        ValueSourceConfiguration string_source = ValueSourceConfiguration.forValue("res1");
        ValueSourceConfiguration config = new ProjectResourceValueSource.StringExpressionSupport().fromPrefixedExpression("#", string_source, TEST_PROJECT);
        Assert.assertNotNull(config);

        Assert.assertEquals(ProjectResourceValueSource.TYPE_ID, config.getType());
        Assert.assertNotNull(config.getSource());
        Assert.assertEquals(StringValueSource.TYPE_ID, config.getSource().getType());
        Assert.assertEquals("res1", config.getSource().getValue());
        }

    @Test
    public void equals()
        {
        testBinaryCondition(new EqualityCondition.StringExpressionSupport());
        }

    @Test
    public void greaterThan()
        {
        testBinaryCondition(new GreaterThanCondition.StringExpressionSupport());
        }

    @Test
    public void lessThan()
        {
        testBinaryCondition(new LessThanCondition.StringExpressionSupport());
        }

    private void testBinaryCondition(BinaryConditionStringExpressionSupport supporter)
        {
        String left = "abc";
        String right = "xyz";
        String to_parse = String.format("\"%s\" %s \"%s\"", left, supporter.getOperator(), right);
        ValueSourceConfiguration parsed = supporter.fromBinaryExpression(ValueSourceConfiguration.forValue(left), supporter.getOperator(), ValueSourceConfiguration.forValue(right), TEST_PROJECT);

        Assert.assertEquals(supporter.getSourceType(), parsed.getType());
        Assert.assertEquals(left, parsed.getSource(BinaryCondition.LEFT_PARAM).getValue());
        Assert.assertEquals(right, parsed.getSource(BinaryCondition.RIGHT_PARAM).getValue());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals(to_parse, stringified);
        }

    @Test
    public void notFromString()
        {
        NotValueSource.StringExpressionSupport supporter = new NotValueSource.StringExpressionSupport();
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue(true);
        arguments.add(subsource);
        ValueSourceConfiguration parsed = supporter.fromArgumentedExpression(supporter.getName(), arguments, TEST_PROJECT);

        Assert.assertEquals(NotValueSource.TYPE_ID, parsed.getType());
        Assert.assertEquals(subsource, parsed.getSource());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals("not(true)", stringified);
        }

    @Test
    public void dateFormat()
        {
        DateFormatValueSource.StringExpressionSupport supporter = new DateFormatValueSource.StringExpressionSupport();
        ValueSourceConfiguration date_config = ValueSourceConfiguration.forValue("date");
        ValueSourceConfiguration format_config = ValueSourceConfiguration.forValue("format");
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        arguments.add(date_config);
        arguments.add(format_config);

        ValueSourceConfiguration parsed = supporter.fromArgumentedExpression(supporter.getName(), arguments, TEST_PROJECT);

        Assert.assertEquals(DateFormatValueSource.TYPE_ID, parsed.getType());
        Assert.assertEquals(date_config, parsed.getSource(DateFormatValueSource.DATE_PARAM));
        Assert.assertEquals(format_config, parsed.getSource(DateFormatValueSource.FORMAT_PARAM));

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals("formatDate(\"date\",\"format\")", stringified);
        }

    @Test
    public void propertySource()
        {
        ValueSourceStringExpressionSupport supporter = new PropertySource.StringExpressionSupport();
        String left = "\"abc\"";
        String right = "\"xyz\"";
        String to_parse = String.format("\"%s\".\"%s\"", left, right);
        ValueSourceConfiguration parsed = supporter.fromDotExpression(ValueSourceConfiguration.forValue(left), ValueSourceConfiguration.forValue(right), TEST_PROJECT);

        Assert.assertEquals(PropertySource.TYPE_ID, parsed.getType());
        Assert.assertEquals(left, parsed.getSource(PropertySource.TARGET_PARAM).getValue());
        Assert.assertEquals(right, parsed.getSource(PropertySource.NAME_PARAM).getValue());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals(to_parse, stringified);
        }

    @Test
    public void listSource()
        {
        ValueSourceStringExpressionSupport supporter = new ListSource.StringExpressionSupport();
        String to_parse = "[11,22]";
        List<ValueSourceConfiguration> elements = new ArrayList<>();
        elements.add(ValueSourceConfiguration.forValue(11L));
        elements.add(ValueSourceConfiguration.forValue(22L));
        ValueSourceConfiguration parsed = supporter.fromArrayExpression(elements, TEST_PROJECT);

        Assert.assertEquals(ListSource.TYPE_ID, parsed.getType());
        Assert.assertEquals(elements.get(0).getValue(), parsed.getSource(0).getValue());
        Assert.assertEquals(elements.get(1).getValue(), parsed.getSource(1).getValue());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assert.assertEquals(to_parse, stringified);
        }

    private static MuseProject TEST_PROJECT = new SimpleProject(new InMemoryResourceStorage());
    }

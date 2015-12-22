package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

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
    public void stringConcatenation2Sources()
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(123L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(456L);
        AdditionSourceStringExpressionSupport supporter = new AdditionSourceStringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(left, "+", right, TEST_PROJECT);

        Assert.assertNotNull(config);
        Assert.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assert.assertEquals(left, config.getSourceList().get(0));
        Assert.assertEquals(right, config.getSourceList().get(1));

        String stringified = supporter.toString(config, TEST_PROJECT);
        Assert.assertEquals("123 + 456", stringified);
        }

    @Test
    public void stringConcatenation3Sources()
        {
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(123L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(456L);
        ValueSourceConfiguration far_right = ValueSourceConfiguration.forValue(789L);
        AdditionSourceStringExpressionSupport supporter = new AdditionSourceStringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(left, "+", right, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", far_right, TEST_PROJECT);

        Assert.assertNotNull(config);
        Assert.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assert.assertEquals(left, config.getSourceList().get(0));
        Assert.assertEquals(right, config.getSourceList().get(1));
        Assert.assertEquals(far_right, config.getSourceList().get(2));

        String stringified = supporter.toString(config, TEST_PROJECT);
        Assert.assertEquals("123 + 456 + 789", stringified);
        }

    @Test
    public void stringConcatenation4Sources()
        {
        ValueSourceConfiguration far_left = ValueSourceConfiguration.forValue(11L);
        ValueSourceConfiguration left = ValueSourceConfiguration.forValue(22L);
        ValueSourceConfiguration right = ValueSourceConfiguration.forValue(33L);
        ValueSourceConfiguration far_right = ValueSourceConfiguration.forValue(44L);
        AdditionSourceStringExpressionSupport supporter = new AdditionSourceStringExpressionSupport();
        ValueSourceConfiguration config = supporter.fromBinaryExpression(far_left, "+", left, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", right, TEST_PROJECT);
        config = supporter.fromBinaryExpression(config, "+", far_right, TEST_PROJECT);

        Assert.assertNotNull(config);
        Assert.assertEquals(AdditionSource.TYPE_ID, config.getType());
        Assert.assertEquals(far_left, config.getSourceList().get(0));
        Assert.assertEquals(left, config.getSourceList().get(1));
        Assert.assertEquals(right, config.getSourceList().get(2));
        Assert.assertEquals(far_right, config.getSourceList().get(3));

        String stringified = supporter.toString(config, TEST_PROJECT);
        Assert.assertEquals("11 + 22 + 33 + 44", stringified);
        }

    @Test
    public void variableSource()
        {
        ValueSourceConfiguration string_source = ValueSourceConfiguration.forValue("var1");
        ValueSourceConfiguration config = new VariableValueSourceStringExpressionSupport().fromPrefixedExpression("$", string_source, TEST_PROJECT);
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
        ValueSourceConfiguration config = new ProjectResourceValueSourceStringExpressionSupport().fromPrefixedExpression("#", string_source, TEST_PROJECT);
        Assert.assertNotNull(config);

        Assert.assertEquals(ProjectResourceValueSource.TYPE_ID, config.getType());
        Assert.assertNotNull(config.getSource());
        Assert.assertEquals(StringValueSource.TYPE_ID, config.getSource().getType());
        Assert.assertEquals("res1", config.getSource().getValue());
        }

    @Test
    public void equals()
        {
        testBinaryCondition(new EqualityConditionStringExpressionSupport());
        }

    @Test
    public void greaterThan()
        {
        testBinaryCondition(new GreaterThanConditionStringExpressionSupport());
        }

    @Test
    public void lessThan()
        {
        testBinaryCondition(new LessThanConditionStringExpressionSupport());
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

        String stringified = supporter.toString(parsed, TEST_PROJECT);
        Assert.assertEquals(to_parse, stringified);
        }

    static MuseProject TEST_PROJECT = new SimpleProject(new InMemoryResourceStore());
    }



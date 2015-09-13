package org.musetest.builtins.tests;

import org.junit.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceQuickEditSupportTests
    {
    @Test
    public void emptyStringSource()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("\"\"", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("string", config.getType());
        Assert.assertNotNull(config.getValue());
        Assert.assertTrue(config.getValue() instanceof String);
        Assert.assertEquals(0, ((String)config.getValue()).length());
        }

    @Test
    public void stringSource()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("\"abc\"", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("string", config.getType());
        Assert.assertNotNull(config.getValue());
        Assert.assertTrue(config.getValue() instanceof String);
        Assert.assertEquals("abc", config.getValue());
        }

    @Test
    public void toFromString()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("string");
        config.setValue("abc123");
        stringifyAndParse(config);
        }

    @Test
    public void stringWithQuote()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("string");
        config.setValue("the var = \"abc\"");
        stringifyAndParse(config);
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
    public void booleanSource()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("true", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("boolean", config.getType());
        Assert.assertNotNull(config.getValue());
        Assert.assertTrue(config.getValue() instanceof Boolean);
        Assert.assertEquals(true, config.getValue());
        }

    @Test
    public void toFromBooleam()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("boolean");
        config.setValue(true);
        stringifyAndParse(config);
        }

    @Test
    public void integerSource()
        {
        List<ValueSourceConfiguration> config_list = ValueSourceQuickEditSupporters.parseWithAll("123", TEST_PROJECT);
        Assert.assertEquals(1, config_list.size());
        ValueSourceConfiguration config = config_list.get(0);
        Assert.assertNotNull(config);
        Assert.assertEquals("integer", config.getType());
        Assert.assertNotNull(config.getValue());
        Assert.assertTrue(config.getValue() instanceof Long);
        Assert.assertEquals(123L, config.getValue());
        }

    @Test
    public void toFromInteger()
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType("integer");
        config.setValue(77L);
        stringifyAndParse(config);
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



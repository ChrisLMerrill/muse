package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill, Copyright 2015 (see LICENSE.txt for license details)
 */
public class ValueSourceConfigTests
    {
    @Test
    public void stringSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"string\", \"value\":\"abc\" }").createSource(null);
        Assert.assertTrue(source instanceof StringValueSource);
        Assert.assertEquals("abc", ((StringValueSource)source).getValue());
        }

    @Test
    public void stringSourceMissingValue() throws IOException, MuseInstantiationException
        {
        try
            {
            ValueSourceConfiguration.fromString("{ \"type\":\"string\"}").createSource(null);
            }
        catch (MuseInstantiationException e)
            {
            return; // this is ok
            }
        Assert.assertTrue("Should have thrown an exception", false);
        }

    @Test
    public void integerSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"integer\", \"value\":123 }").createSource(null);
        Assert.assertTrue(source instanceof IntegerValueSource);
        Assert.assertEquals(new Long(123), ((IntegerValueSource)source).getValue());
        }

    @Test
    public void booleanSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"boolean\", \"value\":true }").createSource(null);
        Assert.assertTrue(source instanceof BooleanValueSource);
        Assert.assertEquals(true, ((BooleanValueSource)source).getValue());
        }

    @Test
    public void variableSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"variable\", \"value\":\"X\" }").createSource(null);
        Assert.assertTrue(source instanceof VariableValueSource);
        MuseValueSource name_source = ((VariableValueSource) source).getName();
        Assert.assertTrue(name_source instanceof StringValueSource);
        Assert.assertEquals("X", ((StringValueSource)name_source).getValue());
        }

    @Test
    public void variableSourceWithValueSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"variable\", \"source\":{\"type\":\"string\", \"value\":\"X\"} }").createSource(null);
        Assert.assertTrue(source instanceof VariableValueSource);
        MuseValueSource name_source = ((VariableValueSource) source).getName();
        Assert.assertTrue(name_source instanceof StringValueSource);
        Assert.assertEquals("X", ((StringValueSource)name_source).getValue());
        }

    @Test
    public void equalsCondition() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"equals\", \"sourceMap\":{\"left\":{ \"type\":\"string\", \"value\":\"abc\" }, \"right\":{ \"type\":\"integer\", \"value\":1 }}}").createSource(null);
        Assert.assertTrue(source instanceof EqualityCondition);

        MuseValueSource left_source = ((EqualityCondition) source).getLeft();
        Assert.assertTrue(left_source instanceof StringValueSource);
        Assert.assertEquals("abc", ((StringValueSource)left_source).getValue());

        MuseValueSource right = ((EqualityCondition) source).getRight();
        Assert.assertTrue(right instanceof IntegerValueSource);
        Assert.assertEquals(new Long(1L), ((IntegerValueSource)right).getValue());
        }

    @Test
    public void greaterThanCondition() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"greaterthan\", \"sourceMap\":{\"left\":{ \"type\":\"integer\", \"value\":7 }, \"right\":{ \"type\":\"integer\", \"value\":1 }}}").createSource(null);
        Assert.assertTrue(source instanceof GreaterThanCondition);

        MuseValueSource left_source = ((GreaterThanCondition) source).getLeft();
        Assert.assertTrue(left_source instanceof IntegerValueSource);
        Assert.assertEquals(new Long(7L), ((IntegerValueSource)left_source).getValue());

        MuseValueSource right = ((GreaterThanCondition) source).getRight();
        Assert.assertTrue(right instanceof IntegerValueSource);
        Assert.assertEquals(new Long(1L), ((IntegerValueSource)right).getValue());
        }

    @Test
    public void lessThanCondition() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"lessthan\", \"sourceMap\":{\"left\":{ \"type\":\"integer\", \"value\":7 }, \"right\":{ \"type\":\"integer\", \"value\":1 }}}").createSource(null);
        Assert.assertTrue(source instanceof LessThanCondition);

        MuseValueSource left_source = ((LessThanCondition) source).getLeft();
        Assert.assertTrue(left_source instanceof IntegerValueSource);
        Assert.assertEquals(new Long(7L), ((IntegerValueSource)left_source).getValue());

        MuseValueSource right = ((LessThanCondition) source).getRight();
        Assert.assertTrue(right instanceof IntegerValueSource);
        Assert.assertEquals(new Long(1L), ((IntegerValueSource)right).getValue());
        }

    @Test
    public void concatenation() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"concatenate\", \"sourceList\":[{ \"type\":\"string\", \"value\":\"abc\" },{ \"type\":\"integer\", \"value\":1 },{ \"type\":\"boolean\", \"value\":true }]}").createSource(null);
        Assert.assertTrue(source instanceof StringConcatenationSource);
        Assert.assertEquals(3, ((StringConcatenationSource)source).getSources().length);

        MuseValueSource first = ((StringConcatenationSource) source).getSources()[0];
        Assert.assertTrue(first instanceof StringValueSource);
        Assert.assertEquals("abc", ((StringValueSource)first).getValue());

        MuseValueSource second = ((StringConcatenationSource) source).getSources()[1];
        Assert.assertTrue(second instanceof IntegerValueSource);
        Assert.assertEquals(new Long(1L), ((IntegerValueSource)second).getValue());

        MuseValueSource third = ((StringConcatenationSource) source).getSources()[2];
        Assert.assertTrue(third instanceof BooleanValueSource);
        Assert.assertEquals(true, ((BooleanValueSource)third).getValue());
        }

    }



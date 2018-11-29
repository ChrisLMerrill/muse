package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill, Copyright 2015 (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")
class ValueSourceConfigTests
    {
    @Test
    void stringSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"string\", \"value\":\"abc\" }").createSource(null);
        Assertions.assertTrue(source instanceof StringValueSource);
        Assertions.assertEquals("abc", ((StringValueSource)source).getValue());
        }

    @Test
    void stringSourceMissingValue() throws IOException
        {
        try
            {
            ValueSourceConfiguration.fromString("{ \"type\":\"string\"}").createSource(null);
            }
        catch (MuseInstantiationException e)
            {
            return; // this is ok
            }
        Assertions.fail("Should have thrown an exception");
        }

    @Test
    void integerSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"integer\", \"value\":123 }").createSource(null);
        Assertions.assertTrue(source instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(123), ((IntegerValueSource)source).getValue());
        }

    @Test
    void booleanSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"boolean\", \"value\":true }").createSource(null);
        Assertions.assertTrue(source instanceof BooleanValueSource);
        Assertions.assertEquals(true, ((BooleanValueSource)source).getValue());
        }

    @Test
    void variableSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"variable\", \"value\":\"X\" }").createSource(null);
        Assertions.assertTrue(source instanceof VariableValueSource);
        MuseValueSource name_source = ((VariableValueSource) source).getName();
        Assertions.assertTrue(name_source instanceof StringValueSource);
        Assertions.assertEquals("X", ((StringValueSource)name_source).getValue());
        }

    @Test
    void variableSourceWithValueSource() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"variable\", \"source\":{\"type\":\"string\", \"value\":\"X\"} }").createSource(null);
        Assertions.assertTrue(source instanceof VariableValueSource);
        MuseValueSource name_source = ((VariableValueSource) source).getName();
        Assertions.assertTrue(name_source instanceof StringValueSource);
        Assertions.assertEquals("X", ((StringValueSource)name_source).getValue());
        }

    @Test
    void equalsCondition() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"equals\", \"sourceMap\":{\"left\":{ \"type\":\"string\", \"value\":\"abc\" }, \"right\":{ \"type\":\"integer\", \"value\":1 }}}").createSource(null);
        Assertions.assertTrue(source instanceof EqualityCondition);

        MuseValueSource left_source = ((EqualityCondition) source).getLeft();
        Assertions.assertTrue(left_source instanceof StringValueSource);
        Assertions.assertEquals("abc", ((StringValueSource)left_source).getValue());

        MuseValueSource right = ((EqualityCondition) source).getRight();
        Assertions.assertTrue(right instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(1L), ((IntegerValueSource)right).getValue());
        }

    @Test
    void greaterThanCondition() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"greaterthan\", \"sourceMap\":{\"left\":{ \"type\":\"integer\", \"value\":7 }, \"right\":{ \"type\":\"integer\", \"value\":1 }}}").createSource(null);
        Assertions.assertTrue(source instanceof GreaterThanCondition);

        MuseValueSource left_source = ((GreaterThanCondition) source).getLeft();
        Assertions.assertTrue(left_source instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(7L), ((IntegerValueSource)left_source).getValue());

        MuseValueSource right = ((GreaterThanCondition) source).getRight();
        Assertions.assertTrue(right instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(1L), ((IntegerValueSource)right).getValue());
        }

    @Test
    void lessThanCondition() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"lessthan\", \"sourceMap\":{\"left\":{ \"type\":\"integer\", \"value\":7 }, \"right\":{ \"type\":\"integer\", \"value\":1 }}}").createSource(null);
        Assertions.assertTrue(source instanceof LessThanCondition);

        MuseValueSource left_source = ((LessThanCondition) source).getLeft();
        Assertions.assertTrue(left_source instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(7L), ((IntegerValueSource)left_source).getValue());

        MuseValueSource right = ((LessThanCondition) source).getRight();
        Assertions.assertTrue(right instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(1L), ((IntegerValueSource)right).getValue());
        }

    @Test
    void concatenation() throws IOException, MuseInstantiationException
        {
        MuseValueSource source = ValueSourceConfiguration.fromString("{ \"type\":\"add\", \"sourceList\":[{ \"type\":\"string\", \"value\":\"abc\" },{ \"type\":\"integer\", \"value\":1 },{ \"type\":\"boolean\", \"value\":true }]}").createSource(null);
        Assertions.assertTrue(source instanceof AdditionSource);
        Assertions.assertEquals(3, ((AdditionSource)source).getSources().length);

        MuseValueSource first = ((AdditionSource) source).getSources()[0];
        Assertions.assertTrue(first instanceof StringValueSource);
        Assertions.assertEquals("abc", ((StringValueSource)first).getValue());

        MuseValueSource second = ((AdditionSource) source).getSources()[1];
        Assertions.assertTrue(second instanceof IntegerValueSource);
        Assertions.assertEquals(Long.valueOf(1L), ((IntegerValueSource)second).getValue());

        MuseValueSource third = ((AdditionSource) source).getSources()[2];
        Assertions.assertTrue(third instanceof BooleanValueSource);
        Assertions.assertEquals(true, ((BooleanValueSource)third).getValue());
        }

    @Test
    void cantAddNullNamedSource()
        {
        ValueSourceConfiguration configuration = ValueSourceConfiguration.forType(StringValueSource.TYPE_ID);
        try
            {
            configuration.addSource("name1", null);
            Assertions.fail("allowed addition of null named source");
            }
        catch (IllegalArgumentException e)
            {
            // expected result
            }
        }
    }



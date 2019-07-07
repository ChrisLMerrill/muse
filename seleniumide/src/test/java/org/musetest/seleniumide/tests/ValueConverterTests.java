package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.javascript.*;
import org.musetest.selenium.values.*;
import org.musetest.seleniumide.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class ValueConverterTests
    {
    @Test
    void simpleText()
        {
        final String content = "abc123";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(StringValueSource.TYPE_ID, source.getType());
        Assertions.assertEquals(content, source.getValue());
        }

    @Test
    void variableReference()
        {
        final String content = "${var1}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(VariableValueSource.TYPE_ID, source.getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, source.getSource().getType());
        Assertions.assertEquals("var1", source.getSource().getValue());
        }

    @Test
    void mixedWithVariableReferenceAtEnd()
        {
        final String content = "abc#${var1}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);

        Assertions.assertEquals(AdditionSource.TYPE_ID, source.getType());

        ValueSourceConfiguration first = source.getSource(0);
        Assertions.assertEquals(StringValueSource.TYPE_ID, first.getType());
        Assertions.assertEquals("abc#", first.getValue());

        ValueSourceConfiguration second = source.getSource(1);
        Assertions.assertEquals(VariableValueSource.TYPE_ID, second.getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, second.getSource().getType());
        Assertions.assertEquals("var1", second.getSource().getValue());
        }

    @Test
    void mixedWithVariableReferenceAtStart()
        {
        final String content = "${var1}abc#";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);

        Assertions.assertEquals(AdditionSource.TYPE_ID, source.getType());

        ValueSourceConfiguration first = source.getSource(0);
        Assertions.assertEquals(VariableValueSource.TYPE_ID, first.getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, first.getSource().getType());
        Assertions.assertEquals("var1", first.getSource().getValue());

        ValueSourceConfiguration second = source.getSource(1);
        Assertions.assertEquals(StringValueSource.TYPE_ID, second.getType());
        Assertions.assertEquals("abc#", second.getValue());

        }

    @Test
    void mixedWithVariableReferenceInMiddle()
        {
        final String content = "abc#${var1} def";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);

        Assertions.assertEquals(AdditionSource.TYPE_ID, source.getType());

        ValueSourceConfiguration first = source.getSource(0);
        Assertions.assertEquals(StringValueSource.TYPE_ID, first.getType());
        Assertions.assertEquals("abc#", first.getValue());

        ValueSourceConfiguration second = source.getSource(1);
        Assertions.assertEquals(VariableValueSource.TYPE_ID, second.getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, second.getSource().getType());
        Assertions.assertEquals("var1", second.getSource().getValue());

        ValueSourceConfiguration third = source.getSource(2);
        Assertions.assertEquals(StringValueSource.TYPE_ID, third.getType());
        Assertions.assertEquals(" def", third.getValue());

        }

    @Test
    void oneKeycode()
        {
        final String content = "${KEY_UP}";
        final String converted = "{UP}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(KeystrokesStringSource.TYPE_ID, source.getType());
        Assertions.assertEquals(converted, source.getSource().getValue());
        }

    @Test
    void keycodes()
        {
        final String content = "${KEY_UP}${KEY_DOWN}${KEY_ENTER}";
        final String converted = "{UP}{DOWN}{ENTER}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(KeystrokesStringSource.TYPE_ID, source.getType());
        Assertions.assertEquals(converted, source.getSource().getValue());
        }

    @Test
    void mixedWithKeycodesInside()
        {
        final String content = "abc${KEY_ENTER}def${KEY_ENTER}xyz";
        final String converted = "abc{ENTER}def{ENTER}xyz";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(KeystrokesStringSource.TYPE_ID, source.getType());
        Assertions.assertEquals(converted, source.getSource().getValue());
        }

    @Test
    void mixedWithKeycodesOutside()
        {
        final String content = "${KEY_ENTER}abc${KEY_ENTER}def${KEY_ENTER}";
        final String converted = "{ENTER}abc{ENTER}def{ENTER}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(KeystrokesStringSource.TYPE_ID, source.getType());
        Assertions.assertEquals(converted, source.getSource().getValue());
        }

    @Test
    void evaluateJavascript()
        {
        final String content = "javascript{'var' + 1;}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(EvaluateJavascriptValueSource.TYPE_ID, source.getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, source.getSource().getType());
        Assertions.assertEquals("'var' + 1;", source.getSource().getValue());
        }
    }



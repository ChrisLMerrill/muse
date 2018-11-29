package org.musetest.seleniumide.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.javascript.*;
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
    void evaluateJavascript()
        {
        final String content = "javascript{'var' + 1;}";
        ValueSourceConfiguration source = ValueConverters.get().convert(content);
        Assertions.assertEquals(EvaluateJavascriptValueSource.TYPE_ID, source.getType());
        Assertions.assertEquals(StringValueSource.TYPE_ID, source.getSource().getType());
        Assertions.assertEquals("'var' + 1;", source.getSource().getValue());
        }
    }



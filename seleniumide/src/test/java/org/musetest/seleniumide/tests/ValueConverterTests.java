package org.musetest.seleniumide.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.seleniumide.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueConverterTests
    {
    @Test
    public void simpleText()
        {
        final String content = "abc123";
        ValueSourceConfiguration source = ValueConverters.get().convertValue(content);
        Assert.assertEquals(StringValueSource.TYPE_ID, source.getType());
        Assert.assertEquals(content, source.getValue());
        }

    @Test
    public void variableReference()
        {
        final String content = "${var1}";
        ValueSourceConfiguration source = ValueConverters.get().convertValue(content);
        Assert.assertEquals(VariableValueSource.TYPE_ID, source.getType());
        Assert.assertEquals(StringValueSource.TYPE_ID, source.getSource().getType());
        Assert.assertEquals("var1", source.getSource().getValue());
        }
    }



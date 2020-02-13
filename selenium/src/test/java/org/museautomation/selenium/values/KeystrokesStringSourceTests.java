package org.museautomation.selenium.values;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.strings.*;
import org.openqa.selenium.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class KeystrokesStringSourceTests
    {
    @Test
    void testSingle() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("{ESCAPE}"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals(Keys.ESCAPE.toString(), result.toString());
        }

    @Test
    void testDouble() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("{ESCAPE}{HOME}"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals(Keys.ESCAPE.toString() + Keys.HOME.toString(), result.toString());
        }

    @Test
    void testSingleMixedInMiddle() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("a{TAB}b"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals("a" + Keys.TAB + "b", result.toString());
        }

    @Test
    void testSingleMixedAtStart() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("{END}Z"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals(Keys.END + "Z", result.toString());
        }

    @Test
    void testSingleMixedAtEnd() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("1{ENTER}"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals("1" + Keys.ENTER, result.toString());
        }

    @Test
    void testDoubleMixedInMiddle() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("1{TAB}{LEFT}2"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals("1" + Keys.TAB + Keys.LEFT + "2", result.toString());
        }

    @Test
    void testControlChar() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forTypeWithSource(KeystrokesStringSource.TYPE_ID, ValueSourceConfiguration.forValue("{CONTROL-A}"));
        Object result = source.createSource(new SimpleProject()).resolveValue(new MockStepExecutionContext());
        Assertions.assertEquals(Keys.chord(Keys.CONTROL, "A"), result.toString());
        }

    @Test
    void fromString()
        {
        MuseProject project = new SimpleProject();
        BaseArgumentedValueSourceStringSupport supporter = new KeystrokesStringSource.StringExpressionSupport();

        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue("{ESCAPE}");
        arguments.add(subsource);
        ValueSourceConfiguration parsed = supporter.fromArgumentedExpression(supporter.getName(), arguments, project);

        Assertions.assertEquals(KeystrokesStringSource.TYPE_ID, parsed.getType());
        Assertions.assertEquals(subsource, parsed.getSource());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(project));
        Assertions.assertEquals("keystrokes(\"{ESCAPE}\")", stringified);
        }
    }



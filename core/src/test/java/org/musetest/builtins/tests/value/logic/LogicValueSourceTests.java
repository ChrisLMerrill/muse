package org.musetest.builtins.tests.value.logic;

import org.junit.jupiter.api.*;
import org.musetest.builtins.value.logic.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.storage.*;
import org.musetest.core.values.*;
import org.musetest.core.values.strings.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LogicValueSourceTests
    {
    @Test
    void orTrueFalse() throws ValueSourceResolutionError, MuseInstantiationException
        {
        Assertions.assertEquals(true, resolveOrSource(true, false));
        }

    @Test
    void orFalseFalse() throws ValueSourceResolutionError, MuseInstantiationException
        {
        Assertions.assertEquals(false, resolveOrSource(false, false));
        }

    @Test
    void orFalseTrue() throws ValueSourceResolutionError, MuseInstantiationException
        {
        Assertions.assertEquals(true, resolveOrSource(false, true));
        }

    @Test
    void orTrueTrue() throws ValueSourceResolutionError, MuseInstantiationException
        {
        Assertions.assertEquals(true, resolveOrSource(true, true));
        }

    private Object resolveOrSource(boolean v1, boolean v2) throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration or = ValueSourceConfiguration.forType(OrValueSource.TYPE_ID);
        or.addSource(ValueSourceConfiguration.forValue(v1));
        or.addSource(ValueSourceConfiguration.forValue(v2));
        MuseValueSource source = or.createSource();
        return source.resolveValue(new MockStepExecutionContext());
        }

    @Test
    void notFromString()
        {
        NotValueSource.StringExpressionSupport supporter = new NotValueSource.StringExpressionSupport();
        List<ValueSourceConfiguration> arguments = new ArrayList<>();
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue(true);
        arguments.add(subsource);
        ValueSourceConfiguration parsed = supporter.fromArgumentedExpression(supporter.getName(), arguments, TEST_PROJECT);

        Assertions.assertEquals(NotValueSource.TYPE_ID, parsed.getType());
        Assertions.assertEquals(subsource, parsed.getSource());

        String stringified = supporter.toString(parsed, new RootStringExpressionContext(TEST_PROJECT));
        Assertions.assertEquals("not(true)", stringified);
        }

    @Test
    void notTrue() throws MuseExecutionError
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(true);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        Assertions.assertEquals(false, source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void notFalse() throws MuseExecutionError
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(false);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        Assertions.assertEquals(true, source.resolveValue(new MockStepExecutionContext()));
        }

    @Test
    void notNull() throws MuseInstantiationException
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue(null);
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        try
            {
            source.resolveValue(new MockStepExecutionContext());
            Assertions.fail("an exception should have been thrown");
            }
        catch (MuseExecutionError e)
            {
            // all good!
            }
        catch (Throwable e)
            {
            Assertions.fail("wrong exception was thrown");
            }
        }

    @Test
    void notString() throws MuseInstantiationException
        {
        ValueSourceConfiguration source_value = ValueSourceConfiguration.forValue("string");
        MuseValueSource source = ValueSourceConfiguration.forTypeWithSource(NotValueSource.TYPE_ID, source_value).createSource();
        try
            {
            source.resolveValue(new MockStepExecutionContext());
            Assertions.fail("an exception should have been thrown");
            }
        catch (MuseExecutionError e)
            {
            // all good!
            }
        catch (Throwable e)
            {
            Assertions.fail("wrong exception was thrown");
            }
        }

    private static MuseProject TEST_PROJECT = new SimpleProject(new InMemoryResourceStorage());
    }
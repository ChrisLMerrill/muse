package org.musetest.builtins.tests;

import kotlin.*;
import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.core.values.strings.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NameValuePairTests
    {
    @Test
    public void returnValue() throws MuseInstantiationException, ValueSourceResolutionError
        {
        MuseValueSource source = _config.createSource(_project);
        Object result = source.resolveValue(new ProjectExecutionContext(_project));

        Assertions.assertTrue(result instanceof Pair);
        Pair pair = (Pair) result;
        Assertions.assertEquals("name1", pair.getFirst());
        Assertions.assertEquals("value1", pair.getSecond());
        }

    @Test
    public void instanceDescription()
        {
        String description = new ValueSourceDescriptors(_project).get(_config).getInstanceDescription(_config, new RootStringExpressionContext(_project));
        Assertions.assertTrue(description.contains("name1"));
        Assertions.assertTrue(description.contains("value1"));
        }

    @BeforeEach
    public void setup()
        {
        _project = new SimpleProject();
        _config = ValueSourceConfiguration.forType(NameValuePair.TYPE_ID);
        _config.addSource(NameValuePair.NAME_PARAM, ValueSourceConfiguration.forValue("name1"));
        _config.addSource(NameValuePair.VALUE_PARAM, ValueSourceConfiguration.forValue("value1"));
        }

    private ValueSourceConfiguration _config;
    private MuseProject _project;
    }



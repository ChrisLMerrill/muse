package org.museautomation.builtins.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.core.values.strings.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class NameValuePairSourceTests
    {
    @Test
    void returnValue() throws MuseInstantiationException, ValueSourceResolutionError
        {
        MuseValueSource source = _config.createSource(_project);
        Object result = source.resolveValue(new ProjectExecutionContext(_project));

        Assertions.assertTrue(result instanceof NameValuePair);
        NameValuePair pair = (NameValuePair) result;
        Assertions.assertEquals("name1", pair.getName());
        Assertions.assertEquals("value1", pair.getValue());
        }

    @Test
    void instanceDescription()
        {
        String description = new ValueSourceDescriptors(_project).get(_config).getInstanceDescription(_config, new RootStringExpressionContext(_project));
        Assertions.assertTrue(description.contains("name1"));
        Assertions.assertTrue(description.contains("value1"));
        }

    @BeforeEach
    void setup()
        {
        _project = new SimpleProject();
        _config = ValueSourceConfiguration.forType(NameValuePairSource.TYPE_ID);
        _config.addSource(NameValuePairSource.NAME_PARAM, ValueSourceConfiguration.forValue("name1"));
        _config.addSource(NameValuePairSource.VALUE_PARAM, ValueSourceConfiguration.forValue("value1"));
        }

    private ValueSourceConfiguration _config;
    private MuseProject _project;
    }



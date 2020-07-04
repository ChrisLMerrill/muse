package org.museautomation.builtins.value;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SplitStringIntoListTests
    {
    @Test
    public void splitString() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(SplitStringIntoListValueSource.TYPE_ID);
        config.addSource(SplitStringIntoListValueSource.SOURCE_PARAM, ValueSourceConfiguration.forValue("1,2,3"));
        config.addSource(SplitStringIntoListValueSource.DELIMITER_PARAM, ValueSourceConfiguration.forValue(","));

        MuseProject project = new SimpleProject();
        MuseValueSource source = new SplitStringIntoListValueSource(config, project);
        Object result = source.resolveValue(new ProjectExecutionContext(project));

        Assertions.assertTrue(result instanceof List);
        List<String> list = (List<String>) result;
        Assertions.assertEquals(3, list.size());
        Assertions.assertEquals("1", list.get(0));
        Assertions.assertEquals("2", list.get(1));
        Assertions.assertEquals("3", list.get(2));
        }
    }
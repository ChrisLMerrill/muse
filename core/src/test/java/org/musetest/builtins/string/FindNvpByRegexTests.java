package org.musetest.builtins.string;

import org.junit.jupiter.api.*;
import org.musetest.builtins.value.*;
import org.musetest.builtins.value.string.*;
import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class FindNvpByRegexTests
    {
    @Test
    void findNvps() throws MuseInstantiationException, ValueSourceResolutionError
        {
        String target = "<a href=\"somepage.html\" id=\"my-id\">the-link</a>";
        String regex = "([a-z]+)=\"([a-z.-]+)\"";

        ValueSourceConfiguration config = ValueSourceConfiguration.forType(FindNvpByRegexSource.TYPE_ID);
        config.addSource(FindNvpByRegexSource.TARGET_PARAM, ValueSourceConfiguration.forValue(target));
        config.addSource(FindNvpByRegexSource.REGEX_PARAM, ValueSourceConfiguration.forValue(regex));

        MuseProject project = new SimpleProject();
        MuseValueSource source = config.createSource(project);
        NameValuePair pair = (NameValuePair) source.resolveValue(new ProjectExecutionContext(project));
        Assertions.assertEquals("href", pair.getName());
        Assertions.assertEquals("somepage.html", pair.getValue());

        // look for second instance
        config.addSource(FindNvpByRegexSource.MATCH_INDEX_PARAM, ValueSourceConfiguration.forValue(1L));
        source = config.createSource(project);
        pair = (NameValuePair) source.resolveValue(new ProjectExecutionContext(project));
        Assertions.assertEquals("id", pair.getName());
        Assertions.assertEquals("my-id", pair.getValue());

        // change the name and value group indexes
        config.addSource(FindNvpByRegexSource.NAME_GROUP_PARAM, ValueSourceConfiguration.forValue(2L));
        config.addSource(FindNvpByRegexSource.VALUE_GROUP_PARAM, ValueSourceConfiguration.forValue(1L));
        source = config.createSource(project);
        pair = (NameValuePair) source.resolveValue(new ProjectExecutionContext(project));
        Assertions.assertEquals("my-id", pair.getName());
        Assertions.assertEquals("id", pair.getValue());
        }
    }
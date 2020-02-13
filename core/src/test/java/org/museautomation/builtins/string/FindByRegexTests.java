package org.museautomation.builtins.string;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.string.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.project.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FindByRegexTests
    {
    @Test
    void findOneValue() throws MuseExecutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(FindByRegexSource.TYPE_ID);
        config.addSource(FindByRegexSource.REGEX_PARAM, ValueSourceConfiguration.forValue("(\\d+)"));
        config.addSource(FindByRegexSource.TARGET_PARAM, ValueSourceConfiguration.forValue("the quick brown fox is 13 years old"));

        MuseValueSource source = new FindByRegexSource(config, _project);
        String found = (String) source.resolveValue(new ProjectExecutionContext(_project));
        Assertions.assertNotNull(found);
        Assertions.assertEquals("13", found);
        }

    private MuseProject _project = new SimpleProject();
    }
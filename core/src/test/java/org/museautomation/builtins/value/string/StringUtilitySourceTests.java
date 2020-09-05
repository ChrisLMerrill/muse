package org.museautomation.builtins.value.string;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.file.*;
import org.museautomation.core.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringUtilitySourceTests
    {
    @Test
    public void cutString() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(CutStringSource.TYPE_ID);
        config.addSource(CutStringSource.SOURCE_PARAM, ValueSourceConfiguration.forValue("https://a.1stdibscdn.com/classical-marble-bust-of-hermes-holding-dionysus-after-the-antique-by-praxiteles-for-sale-picture-2/f_10590/1579695197244/Large_Marble_Sculpture_Male_Bust03_master.jpg?width=768"));
        config.addSource(CutStringSource.MAX_LENGTH_PARAM, ValueSourceConfiguration.forValue(12));
        config.addSource(CutStringSource.FROM_END_PARAM, ValueSourceConfiguration.forValue(true));
        MuseValueSource source = config.createSource();
        Assertions.assertEquals("https://a.1s", source.resolveValue(new MockStepExecutionContext()));

        config.removeSource(CutStringSource.FROM_END_PARAM);
        config.addSource(CutStringSource.FROM_END_PARAM, ValueSourceConfiguration.forValue(false));
        source = config.createSource();
        Assertions.assertEquals("pg?width=768", source.resolveValue(new MockStepExecutionContext()));
        }
    }
package org.museautomation.builtins.value.encoding;

import org.junit.jupiter.api.*;
import org.museautomation.core.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class UrlEncoderValueSourceTests
    {
    @Test
    void basicEncoding() throws MuseInstantiationException, ValueSourceResolutionError
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forTypeWithSource(UrlEncoderValueSource.TYPE_ID, "abc!");
        MuseValueSource source = config.createSource();
        Assertions.assertEquals("abc%21", source.resolveValue(new MockStepExecutionContext()));
        }
    }
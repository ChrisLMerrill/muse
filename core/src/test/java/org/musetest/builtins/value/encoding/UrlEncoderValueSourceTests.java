package org.musetest.builtins.value.encoding;

import org.junit.jupiter.api.*;
import org.musetest.core.*;
import org.musetest.core.mocks.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

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
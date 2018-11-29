package org.musetest.builtins.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-value-source")
@MuseSourceDescriptorImplementation(TestValueSourceDescriptor.class)
public class TestValueSource implements MuseValueSource
    {
    @Override
    public Object resolveValue(MuseExecutionContext context)
        {
        return null;
        }

    @Override
    public String getDescription()
        {
        return null;
        }
    }



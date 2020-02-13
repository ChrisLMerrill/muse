package org.museautomation.builtins.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.values.descriptor.*;

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



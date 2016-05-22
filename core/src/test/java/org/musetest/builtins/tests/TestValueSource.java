package org.musetest.builtins.tests;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("test-value-source")
@MuseSourceDescriptorImplementation(TestValueSourceDescriptor.class)
public class TestValueSource implements MuseValueSource
    {
    @Override
    public Object resolveValue(StepExecutionContext context) throws ValueSourceResolutionError
        {
        return null;
        }

    @Override
    public String getDescription()
        {
        return null;
        }
    }



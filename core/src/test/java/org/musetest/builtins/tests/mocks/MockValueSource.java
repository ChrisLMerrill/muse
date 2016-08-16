package org.musetest.builtins.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("mock-source")
public class MockValueSource implements MuseValueSource
    {
    public MockValueSource(ValueSourceConfiguration config, MuseProject project) throws MuseInstantiationException
        {
        _config = config;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context) throws ValueSourceResolutionError
        {
        return _config.getValue();
        }

    @Override
    public String getDescription()
        {
        return "Mock source";
        }

    private ValueSourceConfiguration _config;

    public final static String TYPE_ID = MockValueSource.class.getAnnotation(MuseTypeId.class).value();
    }



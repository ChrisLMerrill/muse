package org.museautomation.builtins.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("mock-source")
public class MockValueSource implements MuseValueSource
    {
    @SuppressWarnings("unused")
    public MockValueSource(ValueSourceConfiguration config, MuseProject project)
        {
        _config = config;
        }

    @Override
    public Object resolveValue(MuseExecutionContext context)
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



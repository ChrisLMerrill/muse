package org.musetest.core.values.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CompoundSourceFactory implements ValueSourceFactory
    {
    public CompoundSourceFactory(ValueSourceFactory... factories)
        {
        Collections.addAll(_factories, factories);
        }

    @Override
    public MuseValueSource createSource(ValueSourceConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        MuseInstantiationException thrown = null;
        for (ValueSourceFactory factory : _factories)
            {
            MuseValueSource source;
            try
                {
                source = factory.createSource(configuration, project);
                }
            catch (MuseInstantiationException e)
                {
                thrown = e;
                source = null;
                }
            if (source != null)
                return source;
            }

        if (thrown != null)
            throw thrown;

        throw new MuseInstantiationException("No ValueSourceFactory found for type " + configuration.getType());
        }

    private List<ValueSourceFactory> _factories = new ArrayList<>();
    }



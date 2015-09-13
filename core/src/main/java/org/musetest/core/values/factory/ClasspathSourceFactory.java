package org.musetest.core.values.factory;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

import java.lang.reflect.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ClasspathSourceFactory implements ValueSourceFactory
    {
    @Override
    public MuseValueSource createSource(ValueSourceConfiguration configuration, MuseProject project) throws MuseInstantiationException
        {
        String type = configuration.getType();
        if (type == null)
            throw new MuseInstantiationException("ValueSourceConfiguration is missing the required 'type' parameter");
        Class source = new TypeLocator(project).getClassForTypeId(configuration.getType());
        if (source == null)
            return null;

        Constructor constructor;
        try
            {
            constructor = source.getConstructor(ValueSourceConfiguration.class, MuseProject.class);
            }
        catch (NoSuchMethodException e)
            {
            throw new MuseInstantiationException(String.format("ValueSource type '%s' (class %s) does not have a constructor with the required parameters (ValueSourceConfiguration, MuseProject)", type, source.getSimpleName()));
            }
        try
            {
            Object instance = constructor.newInstance(configuration, project);
            if (!(instance instanceof MuseValueSource))
                throw new MuseInstantiationException(String.format("ValueSource type '%s' (class %s) must implement MuseValueSource.", type, source.getSimpleName()));
            return (MuseValueSource) instance;
            }
        catch (Exception e)
            {
            throw new MuseInstantiationException(String.format("Failed to instantiate ValueSource type '%s'. Does the configuration provide all required parameters?", type));
            }
        }
    }



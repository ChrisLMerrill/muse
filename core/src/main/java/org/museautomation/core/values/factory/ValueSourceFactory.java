package org.museautomation.core.values.factory;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill, Copyright 2015 (see LICENSE.txt for license details)
 */
public interface ValueSourceFactory
    {
    MuseValueSource createSource(ValueSourceConfiguration configuration, MuseProject project) throws MuseInstantiationException;

    static ValueSourceFactory getDefault(MuseProject project)
        {
        if (project == null)
            return new CompoundSourceFactory(new ClasspathSourceFactory());
        else
            return new CompoundSourceFactory(new ClasspathSourceFactory());
//            return new CompoundSourceFactory(new ClasspathSourceFactory(), new JavascriptSourceFactory());
        }
    }


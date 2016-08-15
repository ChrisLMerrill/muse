package org.musetest.core.context;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContextInitializer
    {
    void initialize(MuseProject project, MuseExecutionContext context) throws MuseExecutionError;
    }



package org.musetest.core.context;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContextInitializer
    {
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    }



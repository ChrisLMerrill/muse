package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.context.initializers.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContextInitializer
    {
    String getType();  // the unique identifier for this type of context initializer
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    void configure(ContextInitializerConfiguration configuration);
    }



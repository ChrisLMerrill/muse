package org.musetest.core.context.initializers;

import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("WeakerAccess") // publicly extensible UI
public abstract class ContextInitializerConfiguration extends BaseMuseResource
    {
    abstract ContextInitializer createInitializer();

    public static class ContextInitializerResourceType extends ResourceType
        {
        public ContextInitializerResourceType()
            {
            super("context-initializer", "Context Initializer", ContextInitializerConfiguration.class);
            }
        }

    }



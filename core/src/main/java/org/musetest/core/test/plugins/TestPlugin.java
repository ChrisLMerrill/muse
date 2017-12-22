package org.musetest.core.test.plugins;

import org.musetest.core.*;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestPlugin
    {
    String getType();  // the unique identifier for this type of plugin
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    void configure(@Nonnull TestPluginConfiguration configuration);
    }



package org.musetest.core.test.plugin;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestPlugin
    {
    String getType();  // the unique identifier for this type of plugin
    boolean addToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError;
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    }

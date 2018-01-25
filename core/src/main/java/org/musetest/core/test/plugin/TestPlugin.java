package org.musetest.core.test.plugin;

import org.musetest.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestPlugin
    {
    boolean shouldAddToTestContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError;
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    }

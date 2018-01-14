package org.musetest.core.test.plugin;

import org.musetest.core.*;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestPlugin
    {
    String getType();  // the unique identifier for this type of plugin
    boolean applyAutomatically(MuseExecutionContext context) throws MuseExecutionError;
    boolean applyToThisTest(MuseExecutionContext context) throws MuseExecutionError;
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    }

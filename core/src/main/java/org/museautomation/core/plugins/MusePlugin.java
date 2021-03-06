package org.museautomation.core.plugins;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MusePlugin
	{
	boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError;
    void initialize(MuseExecutionContext context) throws MuseExecutionError;
    void shutdown();
	String getId();
	}


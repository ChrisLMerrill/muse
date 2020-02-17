package org.museautomation.builtins.plugins.resultstorage;

import org.museautomation.core.context.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface LocalStorageLocationProvider
	{
	File getBaseFolder();
	File getTaskFolder(TaskExecutionContext task_context);
	}



package org.museautomation.core.resultstorage;

import org.museautomation.core.context.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface LocalStorageLocationProvider
	{
	File getBaseFolder();
	File getTestFolder(TestExecutionContext test_context);
	}



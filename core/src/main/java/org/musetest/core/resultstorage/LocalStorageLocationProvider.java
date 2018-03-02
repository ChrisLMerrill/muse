package org.musetest.core.resultstorage;

import org.musetest.core.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface LocalStorageLocationProvider
	{
	File getBaseFolder();
	File getTestFolder(MuseTest test);
	}



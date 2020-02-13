package org.museautomation.core.util;

import java.io.*;

/**
 * An interface for opening, and re-opening an input stream
 * as necessary.
 * 
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface InputStreamProvider
	{
	InputStream getInputStream() throws IOException;
	}

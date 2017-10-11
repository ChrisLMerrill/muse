package org.musetest.core.datacollection;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestResultData
	{
	String getName();
	void setName(String name);

	void write(OutputStream outstream) throws IOException;
	}


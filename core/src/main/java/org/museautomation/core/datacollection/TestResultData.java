package org.museautomation.core.datacollection;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestResultData
	{
	String getName();
	void setName(@Nonnull String name);

	String suggestFilename();

	void write(@Nonnull OutputStream outstream) throws IOException;
	Object read(@Nonnull InputStream instream) throws IOException;
	}


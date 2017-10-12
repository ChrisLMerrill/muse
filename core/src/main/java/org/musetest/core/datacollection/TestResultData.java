package org.musetest.core.datacollection;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestResultData
	{
	String getName();
	void setName(@Nonnull String name);

	void write(@Nonnull OutputStream outstream) throws IOException;
	}


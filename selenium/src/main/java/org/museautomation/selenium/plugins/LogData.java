package org.museautomation.selenium.plugins;

import org.apache.commons.io.*;
import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LogData implements TestResultData
	{
	public LogData(String type, byte[] bytes)
		{
		_type = type;
		_bytes = bytes;
		}

	@Override
	public String getName()
		{
		return _name;
		}

	@Override
	public void setName(@Nonnull String name)
		{
		_name = name;
		}

	@Override
	public String suggestFilename()
		{
		return _type + "-" + _name + ".txt";
		}

	@Override
	public void write(@Nonnull OutputStream outstream) throws IOException
		{
		IOUtils.copy(new ByteArrayInputStream(_bytes), outstream);
		}

	@Override
	public Object read(@Nonnull InputStream instream) throws IOException
		{
		return null;
		}

	private final byte[] _bytes;
	private String _name = "log";
	private String _type;
	}



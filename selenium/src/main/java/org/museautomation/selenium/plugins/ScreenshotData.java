package org.museautomation.selenium.plugins;

import org.apache.commons.io.*;
import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ScreenshotData implements TaskResultData
	{
	ScreenshotData(byte[] bytes)
		{
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
		return _name + ".png";
		}

	@Override
	public void write(@Nonnull OutputStream outstream) throws IOException
		{
		IOUtils.copy(new ByteArrayInputStream(_bytes), outstream);
		}

	@Override
	public Object read(@Nonnull InputStream instream)
		{
		return null;
		}

	@Override
	public String toString()
		{
		return "(screenshot)";
		}

	private final byte[] _bytes;
	private String _name = "screenshot";
	}



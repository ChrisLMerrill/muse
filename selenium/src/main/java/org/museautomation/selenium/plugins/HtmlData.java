package org.museautomation.selenium.plugins;

import org.apache.commons.io.*;
import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class HtmlData implements TestResultData
	{
	HtmlData(byte[] bytes)
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
		return _name + ".html";
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
		return "(html data)";
		}

	private final byte[] _bytes;
	private String _name = "page-source";
	}



package org.museautomation.core.resultstorage;

import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class MockTestResultData implements TestResultData
	{
	MockTestResultData(String filename, byte[] data)
		{
		_filename = filename;
		_data = data;
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
		return _filename;
		}

	@Override
	public void write(@Nonnull OutputStream outstream) throws IOException
		{
		outstream.write(_data);
		}

	@Override
	public Object read(@Nonnull InputStream instream)
		{
		return null;
		}

	private String _name = "MockTestResultData";
	private final String _filename;
	private final byte[] _data;
	}



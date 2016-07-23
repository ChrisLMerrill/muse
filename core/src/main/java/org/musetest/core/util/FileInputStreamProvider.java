package org.musetest.core.util;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileInputStreamProvider implements InputStreamProvider
	{
	public FileInputStreamProvider(File f)
		{
		if (f==null)
			throw new NullPointerException();
		_file=f;
		}
	public InputStream getInputStream() throws IOException
		{
		return new FileInputStream(_file);
		}
	@Override public boolean equals(Object obj)
		{
		return (obj instanceof FileInputStreamProvider)
			&& _file.equals(((FileInputStreamProvider) obj)._file);
		}
	@Override public int hashCode()
		{
		return FileInputStreamProvider.class.hashCode()
			^ _file.hashCode();
		}
	protected final File _file;
	}

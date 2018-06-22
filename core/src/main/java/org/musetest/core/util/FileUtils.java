package org.musetest.core.util;

import com.google.common.io.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileUtils
	{
	public static String readFileAsString(File file)
		{
		if (!file.exists())
			return String.format("File %s does not exist.", file.getAbsolutePath());
		try (InputStream instream = new FileInputStream(file))
			{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			ByteStreams.copy(instream, bytes);
			String content = bytes.toString();
			bytes.close();
			return content;
			}
		catch (IOException e)
			{
			return String.format("Unable to read file %s. Exception says: %s", file.getName(), e.getMessage());
			}
		}

	public static File createTempFolder(String prefix, String suffix) throws IOException
		{
		final File folder = File.createTempFile(prefix, suffix);
		if (!folder.delete())
			throw new IOException("Unable to delete temp file: " + folder.getPath());
		if (!folder.mkdir())
			throw new IOException("Unable to create temp folder: " + folder.getPath());
		return folder;
		}
	}



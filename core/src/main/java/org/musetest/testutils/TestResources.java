package org.musetest.testutils;

import com.google.common.io.*;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResources
    {
    @SuppressWarnings("null")
    public static File getFile(String path, Class source_class)
        {
        try
            {
            URL resource = source_class.getClassLoader().getResource(path);
            if (resource == null)
                return null;
            return new File(resource.toURI());
            }
        catch (URISyntaxException e)
            {
            throw new RuntimeException("didn't expect to get this from the classloader", e);
            }
        }

	public static String getFileAsString(String path, Class source_class)
		{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final File file = getFile(path, source_class);
		if (file == null)
			return String.format("File does not exist. Looked for %s relative to class %s", path, source_class.getSimpleName());
		try (InputStream instream = new FileInputStream(file))
			{
			ByteStreams.copy(instream, bytes);
			String content = bytes.toString();
			bytes.close();
			return content;
			}
		catch (IOException e)
			{
			return String.format("Unable to read file %s. Exception says %s", file.getName(), e.getMessage());
			}
		}
    }



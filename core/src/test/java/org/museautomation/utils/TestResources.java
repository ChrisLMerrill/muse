package org.museautomation.utils;

import org.museautomation.core.util.*;

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
		final File file = getFile(path, source_class);
		if (file == null)
			return String.format("File does not exist. Looked for %s relative to class %s", path, source_class.getSimpleName());
		return FileUtils.readFileAsString(file);
		}
    }



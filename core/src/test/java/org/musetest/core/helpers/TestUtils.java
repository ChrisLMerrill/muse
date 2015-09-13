package org.musetest.core.helpers;

import java.io.*;
import java.net.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestUtils
    {
    @SuppressWarnings("null")
    public static File getTestResource(String path, Class source_class)
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
    }



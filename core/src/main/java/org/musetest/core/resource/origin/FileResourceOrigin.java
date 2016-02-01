package org.musetest.core.resource.origin;

import org.musetest.core.resource.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class FileResourceOrigin implements ResourceOrigin
    {
    public FileResourceOrigin(File source)
        {
        super();
        _file = source;
        }

    @Override
    public String getDescription()
        {
        return "File: " + _file.getAbsolutePath();
        }

    @Override
    public String suggestId()
        {
        String name = _file.getName();
        if (name.contains("."))
            name = name.substring(0, name.indexOf("."));
        return name;
        }

    @Override
    public InputStream asStream() throws IOException
        {
        return new FileInputStream(_file);
        }

    public File getFile()
        {
        return _file;
        }

    private final File _file;
    }



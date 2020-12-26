package org.museautomation.core.resource.origin;

import org.museautomation.core.resource.*;

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
            name = name.substring(0, name.lastIndexOf("."));
        return name;
        }

    @Override
    public InputStream asInputStream() throws IOException
        {
        return new FileInputStream(_file);
        }

    @Override
    public OutputStream asOutputStream() throws IOException
        {
        return new FileOutputStream(_file);
        }

    public File getFile()
        {
        return _file;
        }

    @Override
    public String toString()
	    {
	    return getDescription();
	    }

    @Override
    public ResourceSerializer getSerializer()
        {
        return _serializer;
        }

    @Override
    public void setSerializer(ResourceSerializer serializer)
        {
        _serializer = serializer;
        }

    private final File _file;
    private ResourceSerializer _serializer;
    }
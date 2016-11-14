package org.musetest.core.resource.origin;

import org.musetest.core.resource.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StringResourceOrigin implements ResourceOrigin
    {
    public StringResourceOrigin(String source)
        {
        if (source == null)
            throw new IllegalArgumentException("source parameter cannot be null");
        _source = source;
        }

    @Override
    public String getDescription()
        {
        return "String";
        }

    @Override
    public String suggestId()
        {
        return Integer.toString(hashCode());
        }

    @Override
    public InputStream asInputStream()
        {
        return new ByteArrayInputStream(_source.getBytes());
        }

    @Override
    public OutputStream asOutputStream() throws IOException
        {
        throw new IllegalArgumentException("Can't output to a string");  // this class is for creating resources from live data.
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

    private String _source;
    private ResourceSerializer _serializer;
    }



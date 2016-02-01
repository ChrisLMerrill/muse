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
    public InputStream asStream()
        {
        return new ByteArrayInputStream(_source.getBytes());
        }

    private String _source;
    }



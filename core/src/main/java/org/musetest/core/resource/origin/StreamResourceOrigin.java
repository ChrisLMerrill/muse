package org.musetest.core.resource.origin;

import org.musetest.core.resource.*;
import org.slf4j.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StreamResourceOrigin implements ResourceOrigin
    {
    public StreamResourceOrigin(InputStream source)
        {
        if (source == null)
            throw new IllegalArgumentException("source parameter cannot be null");
        _stream = source;
        }

    @Override
    public String getDescription()
        {
        return "Streamed resource";
        }

    @Override
    public String suggestId()
        {
        return Integer.toString(hashCode());
        }

    @Override
    public InputStream asInputStream()
        {
        return _stream;
        }

    @Override
    public OutputStream asOutputStream() throws IOException
        {
        throw new IllegalArgumentException("Can't output to an input stream");  // this class is for creating resources from live data.
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

    private InputStream _stream;
    private ResourceSerializer _serializer;

    final static Logger LOG = LoggerFactory.getLogger(StreamResourceOrigin.class);
    }



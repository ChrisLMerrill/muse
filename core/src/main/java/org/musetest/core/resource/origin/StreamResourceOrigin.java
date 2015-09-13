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

    public void close()
        {
        try
            {
            _stream.close();
            }
        catch (IOException e)
            {
            LOG.error("StreamResourceOrigin.close() - Unable to close stream: ", e);
            }
        }

    public InputStream getStream()
        {
        return _stream;
        }

    private InputStream _stream;

    final static Logger LOG = LoggerFactory.getLogger(StreamResourceOrigin.class);
    }



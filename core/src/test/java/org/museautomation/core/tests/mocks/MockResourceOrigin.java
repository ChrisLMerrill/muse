package org.museautomation.core.tests.mocks;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;

import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockResourceOrigin implements ResourceOrigin
    {
    public MockResourceOrigin(MuseResource resource)
        {
        _resource = resource;
        }

    public MuseResource getResource()
        {
        return _resource;
        }

    @Override
    public String getDescription()
        {
        return "MockResourceOrigin: " + _resource;
        }

    @Override
    public InputStream asInputStream() throws IOException
        {
        return null;
        }

    @Override
    public OutputStream asOutputStream() throws IOException
        {
        return null;
        }

    @Override
    public String suggestId()
        {
        return Integer.toString(hashCode());
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

    private final MuseResource _resource;
    private ResourceSerializer _serializer;
    }



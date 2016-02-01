package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.resource.*;

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
    public InputStream asStream() throws IOException
        {
        return null;
        }

    @Override
    public String suggestId()
        {
        return Integer.toString(hashCode());
        }

    private final MuseResource _resource;
    }



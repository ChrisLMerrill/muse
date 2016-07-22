package org.musetest.core.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.resource.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockMuseResource implements MuseResource
    {
    @Override
    public ResourceMetadata getMetadata()
        {
        if (_meta == null)
            _meta = new ResourceMetadata();
        return _meta;
        }

    public String getXyz()
        {
        return _xyz;
        }

    public void setXyz(String xyz)
        {
        _xyz = xyz;
        }

    private String _xyz;
    private ResourceMetadata _meta;
    }



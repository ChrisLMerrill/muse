package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseMuseTest implements MuseTest
    {
    @Override
    public MuseTestResult execute(TestExecutionContext context)
        {
        return executeImplementation(context);
        }

    @Override
    public String getDescription()
        {
        return _meta.getId();
        }

    protected abstract MuseTestResult executeImplementation(TestExecutionContext context);

    @Override
    public ResourceMetadata getMetadata()
        {
        return _meta;
        }

    private ResourceMetadata _meta = new ResourceMetadata(ResourceTypes.Test);
    }



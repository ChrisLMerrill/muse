package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;


/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseMuseTest extends BaseMuseResource implements MuseTest
    {
    @Override
    public MuseTestResult execute(TestExecutionContext context)
        {
        return executeImplementation(context);
        }

    @Override
    public String getDescription()
        {
        return getId();
        }

    protected abstract MuseTestResult executeImplementation(TestExecutionContext context);

    @Override
    public ResourceType getType()
        {
        return new MuseTest.TestResourceType();
        }
    }



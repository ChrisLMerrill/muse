package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MissingTest implements MuseTest
    {
    public MissingTest(String id)
        {
        _id = id;
        }

    @Override
    public MuseTestResult execute(TestExecutionContext context)
        {
        return new BaseMuseTestResult(MuseTestResultStatus.Error, this, new EventLog());
        }

    @Override
    public String getDescription()
        {
        return "MissingTest (id=" + _id + ")";
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return new ResourceMetadata(ResourceTypes.Test);
        }

    private String _id;
    }



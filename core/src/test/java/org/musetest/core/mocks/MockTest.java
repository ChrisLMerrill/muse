package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.test.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTest extends BaseMuseTest
    {
    public MockTest()
        {
        _result = MuseTestResultStatus.Success;
        }

    public MockTest(MuseTestResultStatus result)
        {
        _result = result;
        }

    public MockTest(MuseTestResultStatus result, String id)
        {
        _result = result;
        getMetadata().setId(id);
        }

    @Override
    protected MuseTestResult executeImplementation(TestExecutionContext context)
        {
        return new MuseTestResult()
            {
            @Override
            public MuseTestResultStatus getStatus()
                {
                return _result;
                }

            @Override
            public MuseTest getTest()
                {
                return MockTest.this;
                }

            @Override
            public EventLog getLog()
                {
                return new EventLog();
                }
            };
        }

    private MuseTestResultStatus _result;
    }



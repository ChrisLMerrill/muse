package org.musetest.core.tests.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.suite.*;
import org.musetest.core.test.*;
import org.musetest.core.values.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MockTest extends BaseMuseTest
    {
    public MockTest()
        {
        }

    public MockTest(String id)
        {
        setId(id);
        }

    public MockTest(MuseTestFailureDescription failure)
        {
        _failure = failure;
        }

    public MockTest(MuseTestFailureDescription failure, String id)
        {
        _failure = failure;
        setId(id);
        }

    @Override
    protected MuseTestResult executeImplementation(TestExecutionContext context)
        {
        return new MuseTestResult()
            {
            @Override
            public boolean isPass()
                {
                return _failure == null;
                }

            @Override
            public MuseTestFailureDescription getFailureDescription()
                {
                return _failure;
                }

            @Override
            public String getOneLineDescription()
                {
                return "mock test result";
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

            @Override
            public String getName()
                {
                return "MockTest";
                }

            @Override
            public TestConfiguration getConfiguration()
                {
                return null;
                }

            @Override
            public void setConfiguration(TestConfiguration config)
                {

                }
            };
        }

    @Override
    public Map<String, ValueSourceConfiguration> getDefaultVariables()
        {
        return null;
        }

    @Override
    public void setDefaultVariables(Map<String, ValueSourceConfiguration> default_variables)
        {

        }

    @Override
    public void setDefaultVariable(String name, ValueSourceConfiguration source)
        {

        }

    private MuseTestFailureDescription _failure;
    }



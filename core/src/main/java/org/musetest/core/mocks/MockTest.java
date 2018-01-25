package org.musetest.core.mocks;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.events.*;
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
        setId("mock-test");
        }

    public MockTest(String id)
        {
        setId(id);
        }

    public MockTest(MuseTestFailureDescription failure)
        {
        _failure = new TestResult.Failure(TestResult.FailureType.valueOf(failure.getFailureType().name()), failure.getReason());
        }

    public MockTest(TestResult.Failure failure)
        {
        _failure = failure;
        }

    public MockTest(MuseTestFailureDescription failure, String id)
        {
        this(failure);
        setId(id);
        }

    @Override
    protected boolean executeImplementation(TestExecutionContext context)
        {
        context.raiseEvent(StartTestEventType.create(this, getId()));
        if (_failure != null)
	        {
	        final MuseEvent event = MessageEventType.create(_failure.getDescription());
	        switch (_failure.getType())
		        {
		        case Error:
			        event.addTag(MuseEvent.ERROR);
			        break;
		        case Failure:
			        event.addTag(MuseEvent.FAILURE);
			        break;
		        }
	        context.raiseEvent(event);
	        }
        context.raiseEvent(EndTestEventType.create());
        return true;
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

    private TestResult.Failure _failure;
    }



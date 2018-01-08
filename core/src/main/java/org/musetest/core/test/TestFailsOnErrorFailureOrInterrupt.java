package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.execution.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestFailsOnErrorFailureOrInterrupt implements TestResultProducer
    {
    public TestFailsOnErrorFailureOrInterrupt(MuseTest test, MuseExecutionContext context)
        {
        _test = test;
        _context = context;
        }

    @Override
    public MuseTestResult getTestResult()
        {
        return new BaseMuseTestResult(_test, _context, _failures);
        }

    @Override
    public void eventRaised(MuseEvent event)
        {
        if (event.getTypeId().equals(InterruptedEventType.TYPE_ID))
            _failures.add(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Interrupted, "interrupted by user"));
        else if (event.hasTag(MuseEvent.FAILURE))
            _failures.add(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Failure, event.getAttributeAsString(EndStepEventType.STEP_FAILURE_DESCRIPTION)));
        else if (event.hasTag(MuseEvent.ERROR))
            _failures.add(new MuseTestFailureDescription(MuseTestFailureDescription.FailureType.Error, event.getAttributeAsString(EndStepEventType.STEP_FAILURE_DESCRIPTION)));
        }

    private MuseTest _test;
	private MuseExecutionContext _context;
	private List<MuseTestFailureDescription> _failures = new ArrayList<>();
    }
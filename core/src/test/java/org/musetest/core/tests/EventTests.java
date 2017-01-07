package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.step.*;
import org.musetest.core.tests.mocks.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTests
    {
    @Test
    public void eventDescriptionMatcher()
        {
        EventMatcher matcher = new EventDescriptionMatcher("description");
        Assert.assertTrue(matcher.matches(new MessageEvent("description")));
        Assert.assertFalse(matcher.matches(new MessageEvent("asdf")));
        }

    @Test
    public void eventTypeMatcher()
        {
        EventMatcher matcher = new EventTypeMatcher(MuseEventType.ConditionEvaluated);
        Assert.assertTrue(matcher.matches(new MuseEvent(MuseEventType.ConditionEvaluated)));
        Assert.assertFalse(matcher.matches(new MuseEvent(MuseEventType.EndStep)));
        }

    @Test
    public void stepResultStatusMatcher()
        {
        EventMatcher matcher = new StepResultStatusMatcher(StepExecutionStatus.ERROR);
        Assert.assertTrue(matcher.matches(new StepEvent(MuseEventType.EndStep, new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR))));
        Assert.assertFalse(matcher.matches(new StepEvent(MuseEventType.EndStep, new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE))));
        }
    }



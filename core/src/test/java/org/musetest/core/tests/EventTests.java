package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
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
        EventMatcher matcher = new EventTypeMatcher(ConditionEvaluatedEventType.TYPE_ID);
        Assert.assertTrue(matcher.matches(new MuseEvent(ConditionEvaluatedEventType.TYPE)));
        Assert.assertFalse(matcher.matches(new MuseEvent(StepEvent.END_TYPE)));
        }

    @Test
    public void stepResultStatusMatcher()
        {
        EventMatcher matcher = new StepResultStatusMatcher(StepExecutionStatus.ERROR);
        Assert.assertTrue(matcher.matches(new StepEvent(StepEvent.END_TYPE, new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR))));
        Assert.assertFalse(matcher.matches(new StepEvent(StepEvent.END_TYPE, new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE))));
        }

    @Test
    public void unknownEventType()
        {
        final String type_id = "unknown-type";
        EventType type = EventTypes.DEFAULT.findType(type_id);
        Assert.assertNotNull(type);
        Assert.assertEquals(type_id, type.getTypeId());
        Assert.assertTrue(type.getName().contains(type_id));
        }
    }



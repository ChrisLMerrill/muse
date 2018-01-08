package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.events.matching.*;
import org.musetest.core.mocks.*;
import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class EventTests
    {
    @Test
    public void eventDescriptionMatcher()
        {
        EventMatcher matcher = new EventDescriptionMatcher("description");
        Assert.assertTrue(matcher.matches(MessageEventType.create("description")));
        Assert.assertFalse(matcher.matches(MessageEventType.create("asdf")));
        }

    @Test
    public void eventTypeMatcher()
        {
        EventMatcher matcher = new EventTypeMatcher(ConditionEvaluatedEventType.TYPE_ID);
        Assert.assertTrue(matcher.matches(new MuseEvent(ConditionEvaluatedEventType.INSTANCE)));
        Assert.assertFalse(matcher.matches(new MuseEvent(EndStepEventType.TYPE_ID)));
        }

    @Test
    public void stepResultStatusMatcher()
        {
        EventMatcher matcher = new StepResultStatusMatcher(StepExecutionStatus.ERROR);
        Assert.assertTrue(matcher.matches(EndStepEventType.create(new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR))));
        Assert.assertFalse(matcher.matches(EndStepEventType.create(new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE))));
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
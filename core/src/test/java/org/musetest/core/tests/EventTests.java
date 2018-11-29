package org.musetest.core.tests;

import org.junit.jupiter.api.*;
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
class EventTests
    {
    @Test
    void eventDescriptionMatcher()
        {
        EventMatcher matcher = new EventDescriptionMatcher("description");
        Assertions.assertTrue(matcher.matches(MessageEventType.create("description")));
        Assertions.assertFalse(matcher.matches(MessageEventType.create("asdf")));
        }

    @Test
    void eventTypeMatcher()
        {
        EventMatcher matcher = new EventTypeMatcher(ConditionEvaluatedEventType.TYPE_ID);
        Assertions.assertTrue(matcher.matches(new MuseEvent(ConditionEvaluatedEventType.INSTANCE)));
        Assertions.assertFalse(matcher.matches(new MuseEvent(EndStepEventType.TYPE_ID)));
        }

    @Test
    void stepResultStatusMatcher()
        {
        EventMatcher matcher = new StepResultStatusMatcher(StepExecutionStatus.ERROR);
        Assertions.assertTrue(matcher.matches(EndStepEventType.create(new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.ERROR))));
        Assertions.assertFalse(matcher.matches(EndStepEventType.create(new StepConfiguration(LogMessage.TYPE_ID), new MockStepExecutionContext(), new BasicStepExecutionResult(StepExecutionStatus.COMPLETE))));
        }

    @Test
    void unknownEventType()
        {
        final String type_id = "unknown-type";
        EventType type = EventTypes.DEFAULT.findType(type_id);
        Assertions.assertNotNull(type);
        Assertions.assertEquals(type_id, type.getTypeId());
        Assertions.assertTrue(type.getName().contains(type_id));
        }
    }
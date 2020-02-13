package org.museautomation.core.tests;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.condition.*;
import org.museautomation.builtins.step.*;
import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.step.*;

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
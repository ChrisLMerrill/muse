package org.museautomation.builtins.step;

import org.junit.jupiter.api.*;
import org.museautomation.builtins.value.collection.*;
import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.events.*;
import org.museautomation.core.events.matching.*;
import org.museautomation.core.mocks.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class LogMessageTests
    {
    @Test
    public void basicMessage() throws MuseInstantiationException, ValueSourceResolutionError
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("the message"));
        SteppedTaskExecutionContext context = new MockSteppedTaskExecutionContext();
        LogMessage step = new LogMessage(config, context.getProject());
        step.executeImplementation(new MockStepExecutionContext(context));
        MuseEvent event = context.getEventLog().findEvents(new EventTypeMatcher(MessageEventType.TYPE_ID)).get(0);
        Assertions.assertEquals("the message", new MessageEventType().getDescription(event));
        }

    @Test
    public void messageOneTag() throws MuseInstantiationException, ValueSourceResolutionError
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("the message"));
        config.addSource(LogMessage.TAGS_PARAM, ValueSourceConfiguration.forValue("tag1"));
        SteppedTaskExecutionContext context = new MockSteppedTaskExecutionContext();
        LogMessage step = new LogMessage(config, context.getProject());
        step.executeImplementation(new MockStepExecutionContext(context));
        MuseEvent event = context.getEventLog().findEvents(new EventTagMatcher("tag1")).get(0);
        Assertions.assertEquals("the message", new MessageEventType().getDescription(event));
        }
    @Test
    public void messageMultipleTags() throws MuseInstantiationException, ValueSourceResolutionError
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        config.addSource(LogMessage.MESSAGE_PARAM, ValueSourceConfiguration.forValue("the message"));
        ValueSourceConfiguration list = ValueSourceConfiguration.forType(ListSource.TYPE_ID);
        list.addSource(ValueSourceConfiguration.forValue("tag1"));
        list.addSource(ValueSourceConfiguration.forValue("tag2"));
        config.addSource(LogMessage.TAGS_PARAM, list);
        SteppedTaskExecutionContext context = new MockSteppedTaskExecutionContext();
        LogMessage step = new LogMessage(config, context.getProject());
        step.executeImplementation(new MockStepExecutionContext(context));
        MuseEvent event = context.getEventLog().findEvents(new EventTagMatcher("tag1")).get(0);
        Assertions.assertEquals("the message", new MessageEventType().getDescription(event));
        event = context.getEventLog().findEvents(new EventTagMatcher("tag2")).get(0);
        Assertions.assertEquals("the message", new MessageEventType().getDescription(event));
        }
    }
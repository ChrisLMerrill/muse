package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.condition.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.step.*;
import org.musetest.core.step.events.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")
public class StepConfigurationChangeListenerTests
    {
    @Test
    public void changeType()
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        final String new_type = Wait.TYPE_ID;
        AtomicReference<String> changed_type = new AtomicReference<>(null);

        config.addChangeListener(new StepChangeObserver()
            {
            @Override
            public void typeChanged(TypeChangeEvent event, String old_type, String new_type)
                {
                changed_type.set(new_type);
                }
            });

        config.setType(new_type);
        Assert.assertEquals(new_type, changed_type.get());
        }

    @Test
    public void removeListener()
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        final String new_type = Wait.TYPE_ID;
        AtomicBoolean listener_called = new AtomicBoolean(false);

        StepChangeObserver listener = new StepChangeObserver()
            {
            @Override
            public void typeChanged(TypeChangeEvent event, String old_type, String new_type)
                {
                listener_called.set(true);
                }
            };
        config.addChangeListener(listener);
        config.removeChangeListener(listener);

        config.setType(new_type);
        Assert.assertFalse(listener_called.get());
        }

    @Test
    public void changeMetadata()
        {
        final String description1 = "step description";
        final String description2 = "this is a step";
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        config.setMetadataField(StepConfiguration.META_DESCRIPTION, description1);

        AtomicReference<String> changed_name = new AtomicReference(null);
        AtomicReference<Object> changed_old_value = new AtomicReference(null);
        AtomicReference<Object> changed_new_value = new AtomicReference(null);

        config.addChangeListener(new StepChangeObserver()
            {
            @Override
            public void metadataChanged(MetadataChangeEvent event, String name, Object old_value, Object new_value)
                {
                changed_name.set(name);
                changed_old_value.set(old_value);
                changed_new_value.set(new_value);
                }
            });

        config.setMetadataField(StepConfiguration.META_DESCRIPTION, description2);

        Assert.assertEquals(StepConfiguration.META_DESCRIPTION, changed_name.get());
        Assert.assertEquals(description1, changed_old_value.get());
        Assert.assertEquals(description2, changed_new_value.get());
        }

    @Test
    public void addRemoveSource()
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        final ValueSourceConfiguration old_source = ValueSourceConfiguration.forValue("old message");
        final ValueSourceConfiguration new_source = ValueSourceConfiguration.forValue("new message");
        config.addSource(LogMessage.MESSAGE_PARAM, old_source);

        AtomicReference<String> changed_name = new AtomicReference(null);
        AtomicReference<ValueSourceConfiguration> changed_old_source = new AtomicReference(null);
        AtomicReference<ValueSourceConfiguration> changed_new_source = new AtomicReference(null);

        config.addChangeListener(new StepChangeObserver()
            {
            public void sourceAddedOrRemoved(SourceAddedOrRemovedEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
                {
                changed_name.set(name);
                changed_old_source.set(old_source);
                changed_new_source.set(new_source);
                }
            });

        config.addSource(LogMessage.MESSAGE_PARAM, new_source);

        Assert.assertEquals(LogMessage.MESSAGE_PARAM, changed_name.get());
        Assert.assertEquals(old_source, changed_old_source.get());
        Assert.assertEquals(new_source, changed_new_source.get());

        // remove the source and ensure we get notified
        changed_name.set(null);
        config.addSource(LogMessage.MESSAGE_PARAM, null);
        Assert.assertEquals(LogMessage.MESSAGE_PARAM, changed_name.get());
        Assert.assertEquals(new_source, changed_old_source.get());
        Assert.assertEquals(null, changed_new_source.get());
        }

    @Test
    public void changeSource()
        {
        StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
        final ValueSourceConfiguration source = ValueSourceConfiguration.forValue("old message");
        step.addSource(LogMessage.MESSAGE_PARAM, source);

        AtomicReference<SourceChangedEvent> notified_event = new AtomicReference<>(null);
        AtomicReference<String> notified_name = new AtomicReference<>(null);
        AtomicReference<ValueSourceConfiguration> notified_source = new AtomicReference<>(null);
        step.addChangeListener(new StepChangeObserver()
            {
            @Override
            public void sourceChanged(SourceChangedEvent event, String name, ValueSourceConfiguration source)
                {
                notified_event.set(event);
                notified_name.set(name);
                notified_source.set(source);
                }
            });

        source.setValue("value1");

        Assert.assertNotNull(notified_event.get());
        Assert.assertEquals(LogMessage.MESSAGE_PARAM, notified_name.get());
        Assert.assertEquals(source, notified_source.get());

        // now remove the source and ensure future notifications do not arrive
        notified_event.set(null);
        step.addSource(LogMessage.MESSAGE_PARAM, null);
        source.setValue("value2");
        Assert.assertNull(notified_event.get());

        // add the source back...and get notified.
        notified_event.set(null);
        step.addSource(LogMessage.MESSAGE_PARAM, source);
        source.setValue("value3");
        Assert.assertNotNull(notified_event.get());
        }

    /**
     * does registration of listeners happen correctly when the step is not directly modified (simply comes into being via serialization)
     */
    @Test
    public void changeEventFromDeserializedStepConfig()
        {
        StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
        ValueSourceConfiguration source = ValueSourceConfiguration.forValue("old message");
        step.addSource(LogMessage.MESSAGE_PARAM, source);

        step = Copy.withJsonSerialization(step);
        source = step.getSource(LogMessage.MESSAGE_PARAM);

        AtomicBoolean notified = new AtomicBoolean(false);
        step.addChangeListener(new StepChangeObserver()
            {
            @Override
            public void sourceChanged(SourceChangedEvent event, String name, ValueSourceConfiguration source)
                {
                notified.set(true);
                }
            });

        source.setValue("value2");
        Assert.assertTrue(notified.get());
        }

    @Test
    public void changeSubsource()
        {
        StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue("value #1");
        ValueSourceConfiguration main_source = ValueSourceConfiguration.forSource(VariableValueSource.TYPE_ID, subsource);
        step.addSource(LogMessage.MESSAGE_PARAM, main_source );

        AtomicBoolean notified = new AtomicBoolean(false);
        step.addChangeListener(new StepChangeObserver()
            {
            @Override
            public void sourceChanged(SourceChangedEvent event, String name, ValueSourceConfiguration source)
                {
                notified.set(true);
                }
            });

        subsource.setValue("value #2");
        Assert.assertTrue(notified.get());
        }
    }



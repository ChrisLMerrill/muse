package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.core.step.*;
import org.musetest.core.step.events.*;
import org.musetest.core.values.*;

import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
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
    public void changeSource()
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
            public void sourceChanged(SourceChangeEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
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
        }

    // TODO test change events for changes to the underlying sub-sources of the main sources
    }



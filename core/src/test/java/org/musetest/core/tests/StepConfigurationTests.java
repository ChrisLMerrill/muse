package org.musetest.core.tests;

import org.junit.jupiter.api.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.step.*;
import org.musetest.core.step.events.*;
import org.musetest.core.step.events.TypeChangeEvent;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;

import java.util.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")
public class StepConfigurationTests
    {
    @Test
    public void serializable()
        {
        StepConfiguration original = new StepConfiguration(LogMessage.TYPE_ID);
        original.addSource("name1", ValueSourceConfiguration.forValue("value1"));
        StepConfiguration copy = Copy.withJavaSerialization(original);

        Assertions.assertEquals(original, copy, "Copy is not identical");
        }

    @Test
    public void idNotLong()
        {
        StepConfiguration config = new StepConfiguration("steptype");
        Assertions.assertNull(config.getStepId());

        config.setMetadataField(StepConfiguration.META_ID, "anything not an integer");
        Assertions.assertNull(config.getStepId());

        final Long id = new Random().nextLong();
        config.setStepId(id);
        Assertions.assertEquals(id, config.getStepId());
        }

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
        Assertions.assertEquals(new_type, changed_type.get());
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
        Assertions.assertFalse(listener_called.get());
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

        Assertions.assertEquals(StepConfiguration.META_DESCRIPTION, changed_name.get());
        Assertions.assertEquals(description1, changed_old_value.get());
        Assertions.assertEquals(description2, changed_new_value.get());
        }

    @Test
    public void addRemoveSource()
        {
        StepConfiguration config = new StepConfiguration(LogMessage.TYPE_ID);
        final ValueSourceConfiguration old_source = ValueSourceConfiguration.forValue("old message");
        final ValueSourceConfiguration new_source = ValueSourceConfiguration.forValue("new message");
        config.addSource(LogMessage.MESSAGE_PARAM, old_source);

        AtomicReference<String> changed_name = new AtomicReference(null);
        AtomicReference<ValueSourceConfiguration> replaced_old_source = new AtomicReference(null);
        AtomicReference<ValueSourceConfiguration> replaced_new_source = new AtomicReference(null);
        AtomicReference<ValueSourceConfiguration> removed_source = new AtomicReference(null);

        config.addChangeListener(new NamedSourceChangeObserver()
            {
            @Override
            protected void namedSubsourceReplaced(NamedSourceReplacedEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
                {
                changed_name.set(name);
                replaced_old_source.set(old_source);
                replaced_new_source.set(new_source);
                }

            @Override
            protected void namedSubsourceRemoved(NamedSourceRemovedEvent event, String name, ValueSourceConfiguration source)
                {
                changed_name.set(name);
                removed_source.set(source);
                }
            });

        config.replaceSource(LogMessage.MESSAGE_PARAM, new_source);

        Assertions.assertEquals(LogMessage.MESSAGE_PARAM, changed_name.get());
        Assertions.assertEquals(old_source, replaced_old_source.get());
        Assertions.assertEquals(new_source, replaced_new_source.get());

        // remove the (new) source and ensure we get notified
        changed_name.set(null);
        config.removeSource(LogMessage.MESSAGE_PARAM);
        Assertions.assertEquals(LogMessage.MESSAGE_PARAM, changed_name.get());
        Assertions.assertEquals(new_source, removed_source.get());
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

        Assertions.assertNotNull(notified_event.get());
        Assertions.assertEquals(LogMessage.MESSAGE_PARAM, notified_name.get());
        Assertions.assertEquals(source, notified_source.get());

        // now remove the source and ensure future notifications do not arrive
        notified_event.set(null);
        step.removeSource(LogMessage.MESSAGE_PARAM);
        source.setValue("value2");
        Assertions.assertNull(notified_event.get());

        // add the source back...and get notified.
        notified_event.set(null);
        step.addSource(LogMessage.MESSAGE_PARAM, source);
        source.setValue("value3");
        Assertions.assertNotNull(notified_event.get());
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
        Assertions.assertTrue(notified.get());
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
        Assertions.assertTrue(notified.get());
        }

    @Test
    public void addChildStep()
        {
        StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);

        final AtomicReference<StepConfiguration> added = new AtomicReference<>(null);
        final AtomicReference<Integer> added_index = new AtomicReference<>(null);
        step.addChangeListener(new StepChangeObserver()
            {
            @Override
            protected void childAdded(StepConfiguration child, int index)
	            {
	            added.set(child);
	            added_index.set(index);
	            }
            });

        StepConfiguration added_step = new StepConfiguration(Verify.TYPE_ID);
        step.addChild(added_step);

        Assertions.assertTrue(added.get() == added_step);
        Assertions.assertEquals(0, (int) added_index.get());
        }

    @Test
    public void removeChildStep()
        {
        StepConfiguration step = new StepConfiguration(LogMessage.TYPE_ID);
        StepConfiguration step_to_remove = new StepConfiguration(Verify.TYPE_ID);
        step.addChild(step_to_remove);

        final AtomicReference<StepConfiguration> removed = new AtomicReference<>(null);
        final AtomicReference<Integer> removed_index = new AtomicReference<>(null);
        step.addChangeListener(new StepChangeObserver()
            {
            @Override
            protected void childRemoved(StepConfiguration child, int index)
	            {
	            removed.set(child);
	            removed_index.set(index);
	            }
            });

        step.removeChild(step_to_remove);

        Assertions.assertTrue(removed.get() == step_to_remove);
        Assertions.assertEquals(0, (int) removed_index.get());
        }

    @Test
    public void findParent()
        {
        StepConfiguration root = new StepConfiguration("root");
        root.addChild(new StepConfiguration("child1"));
        final StepConfiguration child2 = new StepConfiguration("child2");
        root.addChild(child2);
        root.addChild(new StepConfiguration("child3"));

        child2.addChild(new StepConfiguration("child2.1"));
        final StepConfiguration child22 = new StepConfiguration("child2.2");
        child2.addChild(child22);

        Assertions.assertNull(root.findParentOf(new StepConfiguration("not_in_tree")));
        Assertions.assertTrue(root == root.findParentOf(child2));
        Assertions.assertTrue(child2 == root.findParentOf(child22));
        }

    @Test
    public void findByStepId()
        {
        MuseProject project = new SimpleProject();
        StepConfiguration root = new StepConfiguration("root");
        root.setStepId(IdGenerator.get(project).generateLongId());

        final StepConfiguration child1 = new StepConfiguration("child1");
        child1.setStepId(IdGenerator.get(project).generateLongId());
        root.addChild(child1);

        final StepConfiguration child2 = new StepConfiguration("child2");
        child2.setStepId(IdGenerator.get(project).generateLongId());
        root.addChild(child2);
        root.addChild(new StepConfiguration("child3"));

        child2.addChild(new StepConfiguration("child2.1"));
        final StepConfiguration child22 = new StepConfiguration("child2.2");
        child22.setStepId(IdGenerator.get(project).generateLongId());
        child2.addChild(child22);

        Assertions.assertNull(root.findByStepId(0L));
        Assertions.assertTrue(root == root.findByStepId(root.getStepId()));
        Assertions.assertTrue(child1 == root.findByStepId(child1.getStepId()));
        Assertions.assertTrue(child2 == root.findByStepId(child2.getStepId()));
        Assertions.assertTrue(child22 == root.findByStepId(child22.getStepId()));
        }

    @Test
    public void tags()
        {
        StepConfiguration step = new StepConfiguration("step-type");
        final String tag1 = "tagname";

        Assertions.assertFalse(step.hasTag(tag1), "step should not initially have any tags");

        boolean added = step.addTag(tag1);
        Assertions.assertTrue(added, "tag was not added");
        Assertions.assertTrue(step.hasTag(tag1), "tag was not added");

        boolean added_dup = step.addTag(tag1);
        Assertions.assertFalse(added_dup, "duplicate tag was allowed");
        Assertions.assertEquals(1, ((Set) step.getMetadata().get(StepConfiguration.META_TAGS)).size(), "duplicate tag was allowed");

        boolean removed = step.removeTag(tag1);
        Assertions.assertTrue(removed, "tag was not removed");
        Assertions.assertFalse(step.hasTag(tag1), "tag was not removed");

        boolean removed_again = step.removeTag(tag1);
        Assertions.assertFalse(removed_again, "can't remove a tag that isn't there");

        final String tag2 = "tagname2";
        step.addTag(tag1);
        step.addTag(tag2);
        Assertions.assertTrue(step.hasTag(tag1),  "tag1 was not added");
        Assertions.assertTrue(step.hasTag(tag2), "tag2 was not added");
        Assertions.assertEquals(2, ((Set) step.getMetadata().get(StepConfiguration.META_TAGS)).size(), "should be 2 entries in list");
        step.removeTag(tag1);
        Assertions.assertFalse(step.hasTag(tag1), "tag1 was not removed");
        Assertions.assertTrue(step.hasTag(tag2), "tag2 was removed");
        Assertions.assertEquals(1, ((Set) step.getMetadata().get(StepConfiguration.META_TAGS)).size(), "should be 1 tag in list");
        step.removeTag(tag2);
        Assertions.assertFalse(step.hasTag(tag2), "tag2 was not removed");
        Assertions.assertTrue(step.getMetadata() == null || step.getMetadata().get(StepConfiguration.META_TAGS) == null, "list should be null");
        }
    }



package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.step.*;
import org.musetest.builtins.value.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;

import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused")
public class ValueSourceChangeListenerTests
    {
    @Test
    public void changeType()
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(NotValueSource.TYPE_ID);
        final AtomicBoolean notified = new AtomicBoolean(false);
        config.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void typeChanged(TypeChangeEvent event, String old_type, String new_type)
                {
                if (old_type.equals(NotValueSource.TYPE_ID) && new_type.equals(VariableValueSource.TYPE_ID))
                    notified.set(true);
                }
            });
        config.setType(VariableValueSource.TYPE_ID);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void changeValue()
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forValue("abc");
        final AtomicBoolean notified = new AtomicBoolean(false);
        config.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void valueChanged(ValueChangeEvent event, Object old_value, Object new_value)
                {
                if (old_value.equals("abc") && new_value.equals(123L))
                    notified.set(true);
                }
            });
        config.setValue(123L);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void changeSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final ValueSourceConfiguration old_subsource = ValueSourceConfiguration.forValue("abc");
        final ValueSourceConfiguration new_subsource = ValueSourceConfiguration.forValue(123L);
        source.setSource(old_subsource);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void subsourceChanged(SingularSubsourceChangeEvent event, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
                {
                if (old_source.equals(old_subsource) && new_source.equals(new_subsource))
                    notified.set(true);
                }
            });
        source.setSource(new_subsource);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void addNamedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final String new_name = "new_source";
        final ValueSourceConfiguration new_subsource = ValueSourceConfiguration.forValue(123L);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void namedSubsourceAdded(NamedSourceAddedEvent event, String name, ValueSourceConfiguration source)
                {
                if (name.equals(new_name) && source.equals(new_subsource))
                    notified.set(true);
                }
            });
        source.addSource(new_name, new_subsource);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void removedNamedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final String removed_name = "old_source";
        final ValueSourceConfiguration removed_subsource = ValueSourceConfiguration.forValue(123L);
        source.addSource(removed_name, removed_subsource);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void namedSubsourceRemoved(NamedSourceRemovedEvent event, String name, ValueSourceConfiguration source)
                {
                if (name.equals(removed_name) && source.equals(removed_subsource))
                    notified.set(true);
                }
            });
        source.removeSource(removed_name);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void renameNamedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final String name1 = "old_source";
        final String name2 = "new_source";
        final ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue(123L);
        source.addSource(name1, subsource);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void namedSubsourceRenamed(NamedSourceRenamedEvent event, String old_name, String new_name, ValueSourceConfiguration source)
                {
                if (old_name.equals(name1) && new_name.equals(name2) && source.equals(subsource))
                    notified.set(true);
                }
            });
        source.renameSource(name1, name2);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void replaceNamedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final String source_name = "source_name";
        final ValueSourceConfiguration original_subsource = ValueSourceConfiguration.forValue("abc");
        final ValueSourceConfiguration replacement_subsource = ValueSourceConfiguration.forValue(123L);
        source.addSource(source_name, original_subsource);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void namedSubsourceReplaced(NamedSourceReplacedEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
                {
                if (source_name.equals(name) && old_source.equals(original_subsource) && new_source.equals(replacement_subsource))
                    notified.set(true);
                }
            });
        source.replaceSource(source_name, replacement_subsource);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void addIndexedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final ValueSourceConfiguration new_subsource = ValueSourceConfiguration.forValue(123L);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void indexedSubsourceAdded(IndexedSourceAddedEvent event, int index, ValueSourceConfiguration source)
                {
                if (index == 0 && source.equals(new_subsource))
                    notified.set(true);
                }
            });
        source.addSource(new_subsource);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void removeIndexedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue(123L);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addSource(subsource);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void indexedSubsourceRemoved(IndexedSourceRemovedEvent event, int index, ValueSourceConfiguration source)
                {
                if (index == 0 && source.equals(subsource))
                    notified.set(true);
                }
            });
        source.removeSource(0);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void replaceIndexedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        final ValueSourceConfiguration original_subsource = ValueSourceConfiguration.forValue("abc");
        final ValueSourceConfiguration replacement_subsource = ValueSourceConfiguration.forValue(123L);
        source.addSource(original_subsource);
        final AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void indexedSubsourceReplaced(IndexedSourceReplacedEvent event, int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
                {
                if (index == 0 && old_source.equals(original_subsource) && new_source.equals(replacement_subsource))
                    notified.set(true);
                }
            });
        source.replaceSource(0, replacement_subsource);
        Assert.assertTrue(notified.get());
        }

    @Test
    public void removeListener()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(IntegerValueSource.TYPE_ID);
        final AtomicBoolean notified = new AtomicBoolean(false);
        ValueSourceChangeObserver listener = new ValueSourceChangeObserver()
            {
            @Override
            public void typeChanged(TypeChangeEvent event, String old_type, String new_type)
                {
                notified.set(true);
                }
            };
        source.addChangeListener(listener);
        source.setType(BooleanValueSource.TYPE_ID);
        Assert.assertTrue(notified.get());

        notified.set(false);
        source.removeChangeListener(listener);
        source.setType(StringValueSource.TYPE_ID);
        Assert.assertFalse(notified.get());
        }

    @Test
    public void oropagateEventFromSingleSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue("var1");
        source.setSource(subsource);

        final AtomicReference<SubsourceModificationEvent> notification = new AtomicReference<>(null);
        ValueSourceChangeObserver listener = new ValueSourceChangeObserver()
            {
            @Override
            public void subsourceModified(SubsourceModificationEvent event)
                {
                notification.set(event);
                }
            };

        source.addChangeListener(listener);
        subsource.setValue("var-changed");
        Assert.assertNotNull(notification.get());
        Assert.assertEquals(source, notification.get().getSource());
        Assert.assertEquals(SubsourceModificationEvent.SubsourceClass.Single, notification.get().getModificationClass());
        Assert.assertNotNull(notification.get().getModificationEvent());
        Assert.assertEquals(subsource, notification.get().getModificationEvent().getSource());

        // change the source and ensure we get the event on the new, but not the old
        notification.set(null);
        ValueSourceConfiguration subsource2 = ValueSourceConfiguration.forValue("var2");
        source.setSource(subsource2);
        subsource.setValue("value2");
        Assert.assertNull(notification.get());
        subsource2.setValue("value3");
        Assert.assertNotNull(notification.get());
        Assert.assertEquals(subsource2, notification.get().getModificationEvent().getSource());

        // now remove the source and ensure further changes to not fire events on the former parent
        notification.set(null);
        source.setSource(null);
        subsource.setValue("value3");
        Assert.assertNull(notification.get());
        }

    @Test
    public void oropagateEventFromIndexedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue("var1");
        source.addSource(0, subsource);

        final AtomicReference<SubsourceModificationEvent> notification = new AtomicReference<>(null);
        ValueSourceChangeObserver listener = new ValueSourceChangeObserver()
            {
            @Override
            public void subsourceModified(SubsourceModificationEvent event)
                {
                notification.set(event);
                }
            };

        source.addChangeListener(listener);
        subsource.setValue("var-changed");
        Assert.assertNotNull(notification.get());
        Assert.assertEquals(source, notification.get().getSource());
        Assert.assertEquals(SubsourceModificationEvent.SubsourceClass.Indexed, notification.get().getModificationClass());
        Assert.assertEquals(0, notification.get().getSubsourceIndex());
        Assert.assertNotNull(notification.get().getModificationEvent());
        Assert.assertEquals(subsource, notification.get().getModificationEvent().getSource());

        // now test the event after we replace the source and then modify it
        notification.set(null);
        ValueSourceConfiguration subsource2 = ValueSourceConfiguration.forValue("var2");
        source.replaceSource(0, subsource2);
        subsource.setValue("ignorethis");
        Assert.assertNull(notification.get());
        subsource2.setValue("changed2");
        Assert.assertEquals(subsource2, notification.get().getModificationEvent().getSource());

        // now test the event after we remove the source and then modify it
        notification.set(null);
        source.removeSource(0);
        subsource.setValue("changed3");
        Assert.assertEquals(null, notification.get());
        }

    @Test
    public void propagateEventFromNamedSubsource()
        {
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(VariableValueSource.TYPE_ID);
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue("var1");
        final String name = "name1";
        source.addSource(name, subsource);

        final AtomicReference<SubsourceModificationEvent> notification = new AtomicReference<>(null);
        ValueSourceChangeObserver listener = new ValueSourceChangeObserver()
            {
            @Override
            public void subsourceModified(SubsourceModificationEvent event)
                {
                notification.set(event);
                }
            };

        source.addChangeListener(listener);
        subsource.setValue("var-changed");
        Assert.assertNotNull(notification.get());
        Assert.assertEquals(source, notification.get().getSource());
        Assert.assertEquals(SubsourceModificationEvent.SubsourceClass.Named, notification.get().getModificationClass());
        Assert.assertEquals(name, notification.get().getSubsourceName());
        Assert.assertNotNull(notification.get().getModificationEvent());
        Assert.assertEquals(subsource, notification.get().getModificationEvent().getSource());

        // now test the event after we re-add another source with the same name and then modify it
        notification.set(null);
        ValueSourceConfiguration subsource2 = ValueSourceConfiguration.forValue("var2");
        source.addSource(name, subsource2);
        subsource.setValue("ignorethis");
        Assert.assertNull(notification.get());
        subsource2.setValue("changed2");
        Assert.assertEquals(subsource2, notification.get().getModificationEvent().getSource());

        // use the replace() method to replace the source and check for correct notification
        notification.set(null);
        ValueSourceConfiguration subsource3 = ValueSourceConfiguration.forValue("var3");
        source.replaceSource(name, subsource3);
        subsource3.setValue("changed3");
        Assert.assertEquals(subsource3, notification.get().getModificationEvent().getSource());

        // remove the source and modify - should not get another notification
        notification.set(null);
        source.removeSource(name);
        Assert.assertNull(notification.get());
        }

    /**
     * does registration of listeners happen correctly when the source is not directly modified (simply comes into being via serialization)
     */
    @Test
    public void changeEventFromDeserializedStepConfig()
        {
        ValueSourceConfiguration subsource = ValueSourceConfiguration.forValue("sub1");
        ValueSourceConfiguration source = ValueSourceConfiguration.forType(LogMessage.TYPE_ID);
        source.addSource(LogMessage.MESSAGE_PARAM, subsource);
        source = Copy.withJsonSerialization(source);
        subsource = source.getSource(LogMessage.MESSAGE_PARAM);

        AtomicBoolean notified = new AtomicBoolean(false);
        source.addChangeListener(new ValueSourceChangeObserver()
            {
            @Override
            public void changed(ValueSourceChangeEvent event)
                {
                notified.set(true);
                }

            @Override
            public void subsourceChanged(SingularSubsourceChangeEvent event, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
                {
                notified.set(true);
                }
            });

        subsource.setValue("value2");
        Assert.assertTrue(notified.get());
        }
    }



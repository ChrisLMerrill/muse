package org.musetest.core.tests;

import org.junit.*;
import org.musetest.builtins.value.*;
import org.musetest.core.values.*;
import org.musetest.core.values.events.*;

import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
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
    }



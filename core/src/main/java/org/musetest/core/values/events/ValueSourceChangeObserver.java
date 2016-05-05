package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * A convenience method for responding to changes to ValueSourceConfigurations.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceChangeObserver implements ValueSourceChangeListener
    {
    @Override
    public void changed(ValueSourceChangeEvent event)
        {
        event.observe(this);
        }

    public void typeChanged(TypeChangeEvent event, String old_type, String new_type) {}
    public void valueChanged(ValueChangeEvent event, Object old_value, Object new_value) {}
    public void subsourceChanged(SingularSubsourceChangeEvent event, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    public void namedSubsourceAdded(NamedSourceAddedEvent event, String name, ValueSourceConfiguration source) {}
    public void namedSubsourceRemoved(NamedSourceRemovedEvent event, String name, ValueSourceConfiguration source) {}
    public void namedSubsourceRenamed(NamedSourceRenamedEvent event, String old_name, String new_name, ValueSourceConfiguration source) {}
    public void namedSubsourceReplaced(NamedSourceReplacedEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    public void indexedSubsourceAdded(IndexedSourceAddedEvent event, int index, ValueSourceConfiguration source) {}
    public void indexedSubsourceRemoved(IndexedSourceRemovedEvent event, int index, ValueSourceConfiguration source) {}
    public void indexedSubsourceReplaced(IndexedSourceReplacedEvent event, int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    public void subsourceModified(SubsourceModificationEvent event) {};
    }



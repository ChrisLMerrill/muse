package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * A convenience class for responding to specific changes to ValueSourceConfigurations.
 * Extend this and override the methods for the events you care about. Register it as
 * the ChangeEventListener on the ValueSourceConfiguration
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceChangeObserver implements ChangeEventListener
    {
    /**
     * Dispatches the event to more specific methods.
     */
    @Override
    public void changeEventRaised(ChangeEvent event)
        {
        if (event instanceof NamedSourceAddedEvent)
            {
            NamedSourceAddedEvent e = (NamedSourceAddedEvent) event;
            namedSubsourceAdded(e, e.getName(), e.getAddedSource());
            }
        else if (event instanceof NamedSourceRemovedEvent)
            {
            NamedSourceRemovedEvent e = (NamedSourceRemovedEvent) event;
            namedSubsourceRemoved(e, e.getName(), e.getRemovedSource());
            }
        else if (event instanceof NamedSourceRenamedEvent)
            {
            NamedSourceRenamedEvent e = (NamedSourceRenamedEvent) event;
            namedSubsourceRenamed(e, e.getOldName(), e.getNewName(), e.getSource());
            }
        else if (event instanceof NamedSourceReplacedEvent)
            {
            NamedSourceReplacedEvent e = (NamedSourceReplacedEvent) event;
            namedSubsourceReplaced(e, e.getName(), e.getOldSource(), e.getNewSource());
            }
        else if (event instanceof IndexedSourceAddedEvent)
            {
            IndexedSourceAddedEvent e = (IndexedSourceAddedEvent) event;
            indexedSubsourceAdded(e, e.getIndex(), e.getAddedSource());
            }
        else if (event instanceof IndexedSourceRemovedEvent)
            {
            IndexedSourceRemovedEvent e = (IndexedSourceRemovedEvent) event;
            indexedSubsourceRemoved(e, e.getIndex(), e.getRemovedSource());
            }
        else if (event instanceof IndexedSourceReplacedEvent)
            {
            IndexedSourceReplacedEvent e = (IndexedSourceReplacedEvent) event;
            indexedSubsourceReplaced(e, e.getIndex(), e.getOldSource(), e.getNewSource());
            }
        else if (event instanceof SingularSubsourceChangeEvent)
            {
            SingularSubsourceChangeEvent e = (SingularSubsourceChangeEvent) event;
            singleSubsourceReplaced(e, e.getOldSource(), e.getNewSource());
            }
        else if (event instanceof ValueChangeEvent)
            {
            ValueChangeEvent e = (ValueChangeEvent) event;
            valueChanged(e, e.getOldValue(), e.getNewValue());
            }
        else if (event instanceof TypeChangeEvent)
            {
            TypeChangeEvent e = (TypeChangeEvent) event;
            typeChanged(e, e.getOldType(), e.getNewType());
            }
        else if (event instanceof SubsourceModificationEvent)
            subsourceChanged((SubsourceModificationEvent) event);
        }

    protected void typeChanged(TypeChangeEvent event, String old_type, String new_type) {}
    protected void valueChanged(ValueChangeEvent event, Object old_value, Object new_value) {}
    protected void singleSubsourceReplaced(SingularSubsourceChangeEvent event, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    protected void subsourceChanged(SubsourceModificationEvent event) {}
    protected void namedSubsourceAdded(NamedSourceAddedEvent event, String name, ValueSourceConfiguration source) {}
    protected void namedSubsourceRemoved(NamedSourceRemovedEvent event, String name, ValueSourceConfiguration source) {}
    protected void namedSubsourceRenamed(NamedSourceRenamedEvent event, String old_name, String new_name, ValueSourceConfiguration source) {}
    protected void namedSubsourceReplaced(NamedSourceReplacedEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    protected void indexedSubsourceAdded(IndexedSourceAddedEvent event, int index, ValueSourceConfiguration source) {}
    protected void indexedSubsourceRemoved(IndexedSourceRemovedEvent event, int index, ValueSourceConfiguration source) {}
    protected void indexedSubsourceReplaced(IndexedSourceReplacedEvent event, int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    }



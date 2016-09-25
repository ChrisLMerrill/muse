package org.musetest.core.step.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepChangeObserver implements ChangeEventListener
    {
    @Override
    public void changeEventRaised(ChangeEvent event)
        {
        if (event instanceof TypeChangeEvent)
            {
            TypeChangeEvent e = (TypeChangeEvent) event;
            typeChanged(e, e.getOldType(), e.getNewType());
            }
        else if (event instanceof MetadataChangeEvent)
            {
            MetadataChangeEvent e = (MetadataChangeEvent) event;
            metadataChanged(e, e.getName(), e.getOldValue(), e.getNewValue());
            }
        else if (event instanceof SourceChangedEvent)
            {
            SourceChangedEvent e = (SourceChangedEvent) event;
            sourceChanged(e, e.getName(), e.getEvent().getSource());
            }
        }

    public void typeChanged(TypeChangeEvent event, String old_type, String new_type) {}
    public void metadataChanged(MetadataChangeEvent event, String name, Object old_value, Object new_value) {}
    public void sourceChanged(SourceChangedEvent event, String name, ValueSourceConfiguration source) {}
    }



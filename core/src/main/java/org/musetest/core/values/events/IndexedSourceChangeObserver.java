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
public class IndexedSourceChangeObserver implements ChangeEventListener
    {
    /**
     * Dispatches the event to more specific methods.
     */
    @Override
    public void changeEventRaised(ChangeEvent event)
        {
        if (!(event instanceof IndexedSourceChangeEvent))
        	return;

        if (event instanceof IndexedSourceAddedEvent)
            {
            IndexedSourceAddedEvent e = (IndexedSourceAddedEvent) event;
            indexedSubsourceAdded(e, e.getIndex(), e.getSource());
            }
        else if (event instanceof IndexedSourceRemovedEvent)
            {
            IndexedSourceRemovedEvent e = (IndexedSourceRemovedEvent) event;
            indexedSubsourceRemoved(e, e.getIndex(), e.getSource());
            }
        else if (event instanceof IndexedSourceReplacedEvent)
            {
            IndexedSourceReplacedEvent e = (IndexedSourceReplacedEvent) event;
            indexedSubsourceReplaced(e, e.getIndex(), e.getSource(), e.getNewSource());
            }
        }

    protected void indexedSubsourceAdded(IndexedSourceAddedEvent event, int index, ValueSourceConfiguration source) {}
    protected void indexedSubsourceRemoved(IndexedSourceRemovedEvent event, int index, ValueSourceConfiguration source) {}
    protected void indexedSubsourceReplaced(IndexedSourceReplacedEvent event, int index, ValueSourceConfiguration source, ValueSourceConfiguration new_source) {}
    }

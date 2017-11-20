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
public class NamedSourceChangeObserver implements ChangeEventListener
    {
    /**
     * Dispatches the event to more specific methods.
     */
    @Override
    public void changeEventRaised(ChangeEvent event)
        {
        if (!(event instanceof NamedSourceChangedEvent))
        	return;

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
            namedSubsourceRenamed(e, e.getOldName(), e.getNewName(), e.getRenamedSource());
            }
        else if (event instanceof NamedSourceReplacedEvent)
            {
            NamedSourceReplacedEvent e = (NamedSourceReplacedEvent) event;
            namedSubsourceReplaced(e, e.getName(), e.getOldSource(), e.getNewSource());
            }
        }

    protected void namedSubsourceAdded(NamedSourceAddedEvent event, String name, ValueSourceConfiguration source) {}
    protected void namedSubsourceRemoved(NamedSourceRemovedEvent event, String name, ValueSourceConfiguration source) {}
    protected void namedSubsourceRenamed(NamedSourceRenamedEvent event, String old_name, String new_name, ValueSourceConfiguration source) {}
    protected void namedSubsourceReplaced(NamedSourceReplacedEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    }



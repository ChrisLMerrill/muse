package org.museautomation.core.values.events;

import org.museautomation.core.util.*;
import org.museautomation.core.values.*;

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
        if (!(event instanceof ValueSourceChangeEvent))
            return;

        if (event instanceof SingularSubsourceChangeEvent)
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
    }

package org.musetest.core.step.events;

import org.musetest.core.step.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepChangeObserver implements StepConfigurationChangeListener
    {
    @Override
    public void changed(StepChangeEvent event)
        {
        event.observe(this);
        }

    public void typeChanged(TypeChangeEvent event, String old_type, String new_type) {}
    public void metadataChanged(MetadataChangeEvent event, String name, Object old_value, Object new_value) {}
    public void sourceChanged(SourceChangeEvent event, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) {}
    }



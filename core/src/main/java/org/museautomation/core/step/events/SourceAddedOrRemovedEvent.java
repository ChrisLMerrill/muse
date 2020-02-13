package org.museautomation.core.step.events;

import org.museautomation.core.step.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SourceAddedOrRemovedEvent extends StepChangeEvent
    {
    public SourceAddedOrRemovedEvent(StepConfiguration step, String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        super(step);
        _name = name;
        _old_source = old_source;
        _new_source = new_source;
        }

    public String getName()
        {
        return _name;
        }

    public ValueSourceConfiguration getOldSource()
        {
        return _old_source;
        }

    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    private final String _name;
    private final ValueSourceConfiguration _old_source;
    private final ValueSourceConfiguration _new_source;
    }



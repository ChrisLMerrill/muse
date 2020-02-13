package org.museautomation.core.values.events;

import org.museautomation.core.values.*;

/**
 * Raised when the primitive value changes.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SingularSubsourceChangeEvent extends ValueSourceChangeEvent
    {
    public SingularSubsourceChangeEvent(ValueSourceConfiguration source, ValueSourceConfiguration new_source, ValueSourceConfiguration old_source)
        {
        super(source);
        _new_source = new_source;
        _old_source = old_source;
        }

    public ValueSourceConfiguration getNewSource()
        {
        return _new_source;
        }

    public ValueSourceConfiguration getOldSource()
        {
        return _old_source;
        }

    private ValueSourceConfiguration _new_source;
    private ValueSourceConfiguration _old_source;
    }



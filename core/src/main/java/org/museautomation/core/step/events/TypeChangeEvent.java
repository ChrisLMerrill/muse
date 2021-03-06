package org.museautomation.core.step.events;

import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TypeChangeEvent extends StepChangeEvent
    {
    public TypeChangeEvent(StepConfiguration step, String old_type, String new_type)
        {
        super(step);
        _old_type = old_type;
        _new_type = new_type;
        }

    @SuppressWarnings("WeakerAccess")
    public String getOldType()
        {
        return _old_type;
        }

    @SuppressWarnings("WeakerAccess")
    public String getNewType()
        {
        return _new_type;
        }

    private final String _old_type;
    private final String _new_type;
    }



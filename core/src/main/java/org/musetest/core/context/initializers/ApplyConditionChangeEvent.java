package org.musetest.core.context.initializers;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ApplyConditionChangeEvent extends ChangeEvent
    {
    ApplyConditionChangeEvent(ContextInitializerConfiguration config, ValueSourceConfiguration old_condition, ValueSourceConfiguration new_condition)
        {
        super(config);
        _old_condition = old_condition;
        _new_condition = new_condition;
        }

    public ContextInitializerConfiguration getConfig()
	    {
	    return (ContextInitializerConfiguration) _target;
	    }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getOldCondition()
        {
        return _old_condition;
        }

    @SuppressWarnings("WeakerAccess")
    public ValueSourceConfiguration getNewCondition()
        {
        return _new_condition;
        }

    private final ValueSourceConfiguration _old_condition;
    private final ValueSourceConfiguration _new_condition;
    }



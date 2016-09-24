package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * Describes a change to a ValueSourceConfiguration.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ValueSourceChangeEvent extends ChangeEvent
    {
    public ValueSourceChangeEvent(ValueSourceConfiguration source)
        {
        super(source);
        }

    public ValueSourceConfiguration getSource()
        {
        return (ValueSourceConfiguration) _target;
        }
    }



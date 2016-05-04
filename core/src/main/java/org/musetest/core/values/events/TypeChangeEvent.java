package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TypeChangeEvent extends ValueSourceChangeEvent
    {
    public TypeChangeEvent(ValueSourceConfiguration source, String new_type, String old_type)
        {
        super(source);
        _new_type = new_type;
        _old_type = old_type;
        }

    public String getNewType()
        {
        return _new_type;
        }

    public String getOldType()
        {
        return _old_type;
        }

    @Override
    protected void observe(ValueSourceChangeObserver observer)
        {
        observer.typeChanged(this, _old_type, _new_type);
        }

    private String _new_type;
    private String _old_type;
    }



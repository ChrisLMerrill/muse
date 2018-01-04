package org.musetest.core.util;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ChangeEvent
    {
    protected ChangeEvent(Object target)
        {
        _target = target;
        }

    public Object getTarget()
	    {
	    return _target;
	    }
    
    protected Object _target;
    }



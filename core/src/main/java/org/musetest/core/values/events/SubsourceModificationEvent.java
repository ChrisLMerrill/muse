package org.musetest.core.values.events;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class SubsourceModificationEvent extends ValueSourceChangeEvent
    {
    public SubsourceModificationEvent(ValueSourceConfiguration source, ValueSourceChangeEvent modification_event)
        {
        super(source);
        _modification_event = modification_event;
        _class = SubsourceClass.Single;
        _subsource_index = -1;
        _subsource_name = null;
        }

    public SubsourceModificationEvent(ValueSourceConfiguration source, int index, ValueSourceChangeEvent modification_event)
        {
        super(source);
        _modification_event = modification_event;
        _class = SubsourceClass.Indexed;
        _subsource_index = index;
        _subsource_name = null;
        }

    public SubsourceModificationEvent(ValueSourceConfiguration source, String name, ValueSourceChangeEvent modification_event)
        {
        super(source);
        _modification_event = modification_event;
        _class = SubsourceClass.Named;
        _subsource_index = -1;
        _subsource_name = name;
        }

    @Override
    protected void observe(ValueSourceChangeObserver observer)
        {
        observer.subsourceModified(this);
        }

    public ValueSourceChangeEvent getModificationEvent()
        {
        return _modification_event;
        }

    public SubsourceClass getModificationClass()
        {
        return _class;
        }

    public int getSubsourceIndex()
        {
        return _subsource_index;
        }

    public String getSubsourceName()
        {
        return _subsource_name;
        }

    final ValueSourceChangeEvent _modification_event;
    final SubsourceClass _class;
    final private int _subsource_index;
    final private String _subsource_name;

    public enum SubsourceClass
        {
        Single,
        Named,
        Indexed
        }
    }



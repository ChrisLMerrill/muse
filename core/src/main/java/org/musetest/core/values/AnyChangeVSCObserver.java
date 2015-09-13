package org.musetest.core.values;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class AnyChangeVSCObserver implements VSCObserver
    {
    public abstract void somethingChanged();

    @Override
    public void typeChanged(String old_type, String new_type)
        {
        somethingChanged();
        }

    @Override
    public void valueChanged(Object old_value, Object new_value)
        {
        somethingChanged();
        }

    @Override
    public void sourceChanged(ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        somethingChanged();
        }

    @Override
    public void mapChanged(Map<String, ValueSourceConfiguration> old_map, Map<String, ValueSourceConfiguration> new_map)
        {
        somethingChanged();
        }

    @Override
    public void listChanged(List<ValueSourceConfiguration> old_list, List<ValueSourceConfiguration> new_list)
        {
        somethingChanged();
        }

    @Override
    public void sourceAddedToList(int index, ValueSourceConfiguration sourceConfiguration)
        {
        somethingChanged();
        }

    @Override
    public void sourceAddedToMap(String name, ValueSourceConfiguration source)
        {
        somethingChanged();
        }

    @Override
    public void sourceRemoved(String name, ValueSourceConfiguration removed)
        {
        somethingChanged();
        }

    @Override
    public void sourceRemoved(int index, ValueSourceConfiguration removed)
        {
        somethingChanged();
        }

    @Override
    public void sourceReplaced(String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        somethingChanged();
        }

    @Override
    public void sourceReplaced(int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source)
        {
        somethingChanged();
        }

    @Override
    public void sourceRenamed(String old_name, String new_name)
        {
        somethingChanged();
        }
    }



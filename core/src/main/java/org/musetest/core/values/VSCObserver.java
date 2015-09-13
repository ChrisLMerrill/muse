package org.musetest.core.values;

import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface VSCObserver
    {
    void typeChanged(String old_type, String new_type);
    void valueChanged(Object old_value, Object new_value);
    void sourceChanged(ValueSourceConfiguration old_source, ValueSourceConfiguration new_source);
    void mapChanged(Map<String, ValueSourceConfiguration> old_map, Map<String, ValueSourceConfiguration> new_map);
    void listChanged(List<ValueSourceConfiguration> old_list, List<ValueSourceConfiguration> new_list);
    void sourceAddedToList(int index, ValueSourceConfiguration sourceConfiguration);
    void sourceAddedToMap(String name, ValueSourceConfiguration source);
    void sourceReplaced(String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source);
    void sourceReplaced(int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source);
    void sourceRemoved(String name, ValueSourceConfiguration removed);
    void sourceRemoved(int index, ValueSourceConfiguration removed);
    void sourceRenamed(String old_name, String new_name);
    }



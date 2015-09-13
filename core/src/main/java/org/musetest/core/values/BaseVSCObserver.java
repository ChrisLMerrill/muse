package org.musetest.core.values;

import java.util.*;

/**
 * Implements all the VSCObserver with no-op methods. Override just the ones you care about.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseVSCObserver implements VSCObserver
    {
    public void typeChanged(String old_type, String new_type) { }
    public void valueChanged(Object old_value, Object new_value) { }
    public void sourceChanged(ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) { }
    public void mapChanged(Map<String, ValueSourceConfiguration> old_map, Map<String, ValueSourceConfiguration> new_map) { }
    public void listChanged(List<ValueSourceConfiguration> old_list, List<ValueSourceConfiguration> new_list) { }
    public void sourceAddedToList(int index, ValueSourceConfiguration sourceConfiguration) { }
    public void sourceAddedToMap(String name, ValueSourceConfiguration source) { }
    public void sourceRemoved(String name, ValueSourceConfiguration removed) { }
    public void sourceRemoved(int index, ValueSourceConfiguration removed) { }
    public void sourceReplaced(String name, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) { }
    public void sourceReplaced(int index, ValueSourceConfiguration old_source, ValueSourceConfiguration new_source) { }
    public void sourceRenamed(String old_name, String new_name) { }
    }



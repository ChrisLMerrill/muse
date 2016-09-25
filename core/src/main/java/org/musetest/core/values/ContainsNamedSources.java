package org.musetest.core.values;

import org.musetest.core.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContainsNamedSources
    {
    ValueSourceConfiguration getSource(String name);
    void addSource(String name, ValueSourceConfiguration source);
    ValueSourceConfiguration removeSource(String name);
    boolean renameSource(String old_name, String new_name);
    ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source);
    void addChangeListener(ChangeEventListener listener);
    }


package org.museautomation.core.values;

import org.museautomation.core.util.*;

import java.util.*;

/**
 * Contains a map of named ValueSourceConfigurations that can be edited. Raises change events
 * that can be listened to.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContainsNamedSources
    {
    ValueSourceConfiguration getSource(String name);
    void addSource(String name, ValueSourceConfiguration source);
    ValueSourceConfiguration removeSource(String name);
    boolean renameSource(String old_name, String new_name);
    ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source);
    Set<String> getSourceNames();
    void addChangeListener(ChangeEventListener listener);
    boolean removeChangeListener(ChangeEventListener listener);
    }


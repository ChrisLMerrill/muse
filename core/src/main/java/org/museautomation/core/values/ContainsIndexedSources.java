package org.museautomation.core.values;

import org.museautomation.core.util.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface ContainsIndexedSources
	{
	void addSource(ValueSourceConfiguration source);
	void addSource(int index, ValueSourceConfiguration source);  // TODO rename to insert
	ValueSourceConfiguration getSource(int index);
	ValueSourceConfiguration removeSource(int index);
	ValueSourceConfiguration replaceSource(int index, ValueSourceConfiguration new_source);
	List<ValueSourceConfiguration> getSourceList();
	void addChangeListener(ChangeEventListener listener);
    boolean removeChangeListener(ChangeEventListener listener);
	}

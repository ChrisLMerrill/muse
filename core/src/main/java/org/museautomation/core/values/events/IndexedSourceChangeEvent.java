package org.museautomation.core.values.events;

import org.museautomation.core.util.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class IndexedSourceChangeEvent extends ChangeEvent
	{
	IndexedSourceChangeEvent(ContainsIndexedSources target, int index, ValueSourceConfiguration source)
		{
		super(target);
		_index = index;
		_source = source;
		}

	public int getIndex()
		{
		return _index;
		}

	public ValueSourceConfiguration getSource()
		{
		return _source;
		}

	private int _index;
	private ValueSourceConfiguration _source;
	}

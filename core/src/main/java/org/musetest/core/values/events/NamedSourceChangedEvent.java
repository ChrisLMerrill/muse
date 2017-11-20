package org.musetest.core.values.events;

import org.musetest.core.util.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class NamedSourceChangedEvent extends ChangeEvent
	{
	NamedSourceChangedEvent(ContainsNamedSources target, String name, ValueSourceConfiguration added_source)
		{
		super(target);
		_name = name;
		_source = added_source;
		}

	protected String _name;
	ValueSourceConfiguration _source;
	}



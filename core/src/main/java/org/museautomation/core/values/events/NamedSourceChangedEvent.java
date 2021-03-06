package org.museautomation.core.values.events;

import org.museautomation.core.util.*;
import org.museautomation.core.values.*;

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

	public String getName()
		{
		return _name;
		}

	protected String _name;
	ValueSourceConfiguration _source;
	}



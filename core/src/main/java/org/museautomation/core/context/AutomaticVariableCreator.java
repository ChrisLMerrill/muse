package org.museautomation.core.context;

import java.util.*;

/**
 * Creates a variable name based on prefix and index.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class AutomaticVariableCreator
	{
	public AutomaticVariableCreator(CheckExisting checker)
		{
		_checker = checker;
		}

	public synchronized String createNextName(String prefix)
		{
		Long index = _prefix_indexes.get(prefix);
		if (index == null)
			index = 1L;
		String name = prefix + index;
		while (_checker.exists(name))
			{
			index++;
			name = prefix + index;
			}
		_prefix_indexes.put(prefix, index);
		return name;
		}

	private final CheckExisting _checker;
	private Map<String, Long> _prefix_indexes = new HashMap<>();

	public interface CheckExisting
		{
		boolean exists(String name);
		}
	}



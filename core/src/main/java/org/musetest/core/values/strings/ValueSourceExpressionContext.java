package org.musetest.core.values.strings;

import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ValueSourceExpressionContext extends ChildExpressionContext
	{
	public ValueSourceExpressionContext(StringExpressionContext parent, ValueSourceConfiguration parent_source)
		{
		super(parent);
		_parent_source = parent_source;
		}

	@Override
	public ValueSourceConfiguration getParentSource()
		{
		return _parent_source;
		}

	private final ValueSourceConfiguration _parent_source;
	}



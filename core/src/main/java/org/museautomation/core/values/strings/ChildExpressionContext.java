package org.museautomation.core.values.strings;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class ChildExpressionContext implements StringExpressionContext
	{
	public ChildExpressionContext(StringExpressionContext parent)
		{
		_parent = parent;
		}

	@Override
	public MuseProject getProject()
		{
		return _parent.getProject();
		}

	@Override
	public StringExpressionContext getParentContext()
		{
		return _parent;
		}

	private final StringExpressionContext _parent;
	}



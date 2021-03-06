package org.museautomation.core.values.strings;

import org.museautomation.core.*;
import org.museautomation.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class RootStringExpressionContext implements StringExpressionContext
	{
	public RootStringExpressionContext(MuseProject project)
		{
		_project = project;
		}

	@Override
	public MuseProject getProject()
		{
		return _project;
		}

	@Override
	public StringExpressionContext getParentContext()
		{
		return null;
		}

	@Override
	public ValueSourceConfiguration getParentSource()
		{
		return null;
		}

	private final MuseProject _project;
	}



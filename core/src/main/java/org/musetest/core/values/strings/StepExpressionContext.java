package org.musetest.core.values.strings;

import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.step.*;
import org.musetest.core.values.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepExpressionContext extends ChildExpressionContext
	{
	public StepExpressionContext(MuseProject project, StepConfiguration step)
		{
		super(new RootStringExpressionContext(project));
		_step = step;
		}

	public StepExpressionContext(StringExpressionContext parent, StepConfiguration step)
		{
		super(parent);
		_step = step;
		}

	@Override
	public ValueSourceConfiguration getParentSource()
		{
		return null;
		}

	public StepConfiguration getStep()
		{
		return _step;
		}

	private final StepConfiguration _step;
	}

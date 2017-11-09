package org.musetest.core.step.events;

import org.musetest.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ChildRemovedEvent extends StepChangeEvent
	{
	public ChildRemovedEvent(StepConfiguration step, StepConfiguration child, int index)
		{
		super(step);
		_child = child;
		_index = index;
		}

	public StepConfiguration getRemovedChild()
		{
		return _child;
		}

	public int getIndex()
		{
		return _index;
		}

	private final StepConfiguration _child;
	private final int _index;
	}



package org.museautomation.core.step.events;

import org.museautomation.core.step.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ChildAddedEvent extends StepChangeEvent
	{
	public ChildAddedEvent(StepConfiguration step, StepConfiguration child, int index)
		{
		super(step);
		_child = child;
		_index = index;
		}

	public StepConfiguration getAddedChild()
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



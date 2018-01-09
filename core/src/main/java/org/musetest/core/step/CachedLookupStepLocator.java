package org.musetest.core.step;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class CachedLookupStepLocator implements StepLocator
	{
	@Override
	public StepConfiguration findStep(Long id)
		{
		return _steps.get(id);
		}

	@Override
	public void loadSteps(StepConfiguration root)
		{
		_steps.put(root.getStepId(), root);
		if (root.getChildren() != null)
			for (StepConfiguration child : root.getChildren())
				loadSteps(child);
		}

	@Override
	public void loadSteps(List<StepConfiguration> steps)
		{
		for (StepConfiguration step : steps)
			loadSteps(step);
		}

	private Map<Long, StepConfiguration> _steps = new HashMap<>();
	}



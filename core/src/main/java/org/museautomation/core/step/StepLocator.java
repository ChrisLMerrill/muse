package org.museautomation.core.step;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface StepLocator
	{
	@SuppressWarnings("unused")  // provided for extensions
	StepConfiguration findStep(Long id);
	void loadSteps(StepConfiguration root);
	void loadSteps(List<StepConfiguration> steps);
	}



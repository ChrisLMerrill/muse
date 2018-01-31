package org.musetest.core.datacollection;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface DataCollector
	{
	/**
	 * Get the test data that was collected. Null is allowed if the collector did not collect any data.
	 */
	@Nullable
	TestResultData getData();
	}



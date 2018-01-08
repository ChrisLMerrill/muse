package org.musetest.core.datacollection;

import org.musetest.core.test.plugins.*;

import javax.annotation.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface DataCollector extends TestPlugin
	{
	/**
	 * Get the test data that was collected. Null is allowed if the collector did not collect any data.
	 */
	@Nullable
	TestResultData getData();
	}



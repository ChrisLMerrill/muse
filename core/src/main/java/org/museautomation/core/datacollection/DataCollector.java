package org.museautomation.core.datacollection;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface DataCollector
	{
	/**
	 * Get the test data that was collected. An empty list must be returned if the collector did not collect any data.
	 */
	List<TestResultData> getData();
	}



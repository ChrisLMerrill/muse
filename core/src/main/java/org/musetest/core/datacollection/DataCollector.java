package org.musetest.core.datacollection;

import org.musetest.core.test.plugins.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface DataCollector extends TestPlugin
	{
	TestResultData getData();
	}



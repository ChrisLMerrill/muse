package org.musetest.core.datacollection;

import org.musetest.core.context.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DataCollectionConfigurations
	{
	boolean isAppliedToTest(TestExecutionContext context)
		{
		// TODO
		return true;
		}

	public ValueSourceConfiguration getApplyToTestCondition()
		{
		return _apply_to_test_condition;
		}

	public void setApplyToTestCondition(ValueSourceConfiguration apply_to_test_condition)
		{
		_apply_to_test_condition = apply_to_test_condition;
		}

	ValueSourceConfiguration _apply_to_test_condition;
	List<DataCollectorConfiguration> _collector_configs;
	}
package org.musetest.core.datacollection;

import org.musetest.core.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DataCollectorConfiguration
	{
	DataCollector createCollector(MuseExecutionContext context)
		{
		// TODO
		return null;
		}


	// required for de/serialization
	private String getCollectorType()
		{
		return _collector_type;
		}

	// required for de/serialization
	private void setCollectorType(String collector_type)
		{
		_collector_type = collector_type;
		}

	// required for de/serialization
	private Map<String, ValueSourceConfiguration> getParameters()
		{
		return _parameters;
		}

	// required for de/serialization
	private void setParameters(Map<String, ValueSourceConfiguration> parameters)
		{
		_parameters = parameters;
		}

	public ValueSourceConfiguration getCreateCondition()
		{
		return _create_condition;
		}

	public void setCreateCondition(ValueSourceConfiguration create_condition)
		{
		_create_condition = create_condition;
		}

	private Map<String, ValueSourceConfiguration> _parameters;
	private String _collector_type;
	private ValueSourceConfiguration _create_condition;
	}



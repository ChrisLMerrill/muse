package org.museautomation.builtins.plugins.resultstorage;

import org.museautomation.core.*;
import org.museautomation.core.datacollection.*;
import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
class MockTestResultProducer implements DataCollector, MusePlugin
	{
	@Override
	public boolean conditionallyAddToContext(MuseExecutionContext context, boolean automatic)
		{
		return true;
		}

	@Override
	public void initialize(MuseExecutionContext context)
		{

		}

	@Override
	public void shutdown()
		{

		}

	@Override
	public String getId()
		{
		return "MockTestResultProducer";
		}

	MockTestResultProducer(List<TaskResultData> data)
		{
		_data = data;
		}

	@Override
	public List<TaskResultData> getData()
		{
		return _data;
		}

	private final List<TaskResultData> _data;
	}



package org.museautomation.core.resultstorage;

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

	MockTestResultProducer(List<TestResultData> data)
		{
		_data = data;
		}

	@Override
	public List<TestResultData> getData()
		{
		return _data;
		}

	private final List<TestResultData> _data;
	}



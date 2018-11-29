package org.musetest.core.resultstorage;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.plugins.*;

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



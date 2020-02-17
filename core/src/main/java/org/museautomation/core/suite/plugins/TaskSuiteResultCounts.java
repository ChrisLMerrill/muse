package org.museautomation.core.suite.plugins;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.museautomation.core.*;
import org.museautomation.core.datacollection.*;

import javax.annotation.*;
import java.io.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TaskSuiteResultCounts implements TaskResultData
	{
	@Override
	public String getName()
		{
		return _name;
		}

	@Override
	public void setName(@Nonnull String name)
		{
		_name = name;
		}

	@Override
	public String suggestFilename()
		{
		return "TestSuiteResultCounts.json";
		}

	public int getSuccesses()
		{
		return _successes;
		}

	public void setSuccesses(int successes)
		{
		_successes = successes;
		}

	public int getErrors()
		{
		return _errors;
		}

	public void setErrors(int errors)
		{
		_errors = errors;
		}

	public int getFailures()
		{
		return _failures;
		}

	public void setFailures(int failures)
		{
		_failures = failures;
		}

	@Override
	public void write(@Nonnull OutputStream outstream) throws IOException
		{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(outstream, this);
		}

	@Override
	public Object read(@Nonnull InputStream instream) throws IOException
		{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(instream, TaskResult.class);
		}

	@JsonIgnore
	public int getTotalTasks()
		{
		return _successes + _errors + _failures;
		}

	private String _name = "Test Suite Result Counters";

	int _successes = 0;
	int _errors = 0;
	int _failures = 0;
	}
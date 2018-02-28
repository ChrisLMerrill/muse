package org.musetest.core;

import com.fasterxml.jackson.databind.*;
import org.musetest.core.context.*;
import org.musetest.core.datacollection.*;

import javax.annotation.*;
import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class TestResult implements TestResultData
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

	public String getTestId()
		{
		return _test_id;
		}

	public void setTestId(String test_id)
		{
		_test_id = test_id;
		}

	public boolean isPass()
		{
		return _pass;
		}

	public void setPass(boolean pass)
		{
		_pass = pass;
		}

	@SuppressWarnings("unused")  // required for JSON serialization
	public String getSummary()
		{
		return _summary;
		}

	public void setSummary(String summary)
		{
		_summary = summary;
		}

	public List<Failure> getFailures()
		{
		return _failures;
		}

	public void setFailures(List<Failure> failures)
		{
		_failures = failures;
		}

	public boolean hasErrors()
		{
		for (Failure failure : _failures)
			if (failure.getType().equals(FailureType.Error))
				return true;
		return false;
		}

	public boolean hasFailures()
		{
		for (Failure failure : _failures)
			if (failure.getType().equals(FailureType.Failure))
				return true;
		return false;
		}

	@Override
	public String suggestFilename()
		{
		return "result.json";
		}

	@Override
	public void write(@Nonnull OutputStream outstream) throws IOException
		{
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(outstream, this);
		}

	@Override
	public TestResult read(@Nonnull InputStream instream) throws IOException
		{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(instream, TestResult.class);
		}

	public void addFailure(Failure failure)
		{
		_failures.add(failure);
		}

	@Override
	public boolean equals(Object obj)
		{
		if (!(obj instanceof TestResult))
			return true;

		TestResult other = (TestResult) obj;
		return Objects.equals(_name, other._name)
			&& Objects.equals(_pass, other._pass)
			&& Objects.equals(_test_id, other._test_id)
			&& Objects.equals(_summary, other._summary)
			&& Objects.equals(_failures, other._failures);
		}

	public static TestResult create(String name, String test_id, String summary, FailureType failure, String message)
		{
		TestResult result = new TestResult();
		result.setName(name);
		result.setTestId(test_id);
		result.setSummary(summary);
		result.addFailure(new Failure(failure, message));
		result.setPass(false);
		return result;
		}

	public static TestResult create(String name, String test_id, String summary)
		{
		TestResult result = new TestResult();
		result.setName(name);
		result.setTestId(test_id);
		result.setSummary(summary);
		return result;
		}

	public static TestResult find(TestExecutionContext context)
		{
		for (DataCollector collector : DataCollectors.list(context))
			if (collector.getData() instanceof TestResult)
				return (TestResult) collector.getData();
		return null;
		}

	private String _name;
	private String _test_id;
	private Boolean _pass = true;
	private String _summary;
	private List<Failure> _failures = new ArrayList<>();

	public enum FailureType
		{
		Failure,
		Error,
		Interrupted
		}

	public static class Failure
		{
		public Failure()
			{
			}

		public Failure(FailureType type, String description)
			{
			_type = type;
			_description = description;
			}

		public FailureType getType()
			{
			return _type;
			}

		public void setType(FailureType type)
			{
			_type = type;
			}

		public String getDescription()
			{
			return _description;
			}

		public void setDescription(String description)
			{
			_description = description;
			}

		@Override
		public boolean equals(Object obj)
			{
			if (!(obj instanceof Failure))
				return false;

			Failure other = (Failure) obj;
			return Objects.equals(_type, other._type)
				&& Objects.equals(_description, other._description);
			}

		private FailureType _type;
		private String _description;
		}
	}

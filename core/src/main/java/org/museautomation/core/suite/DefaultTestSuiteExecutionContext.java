package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.context.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestSuiteExecutionContext extends BaseExecutionContext implements TestSuiteExecutionContext
	{
	public DefaultTestSuiteExecutionContext(MuseProject project, MuseTestSuite suite)
		{
		super(project, ContextVariableScope.Suite);
		_suite = suite;
		}

	public DefaultTestSuiteExecutionContext(MuseExecutionContext parent, MuseTestSuite suite)
		{
		super(parent, ContextVariableScope.Suite);
		_suite = suite;
		}

	@Override
	public MuseTestSuite getSuite()
		{
		return _suite;
		}

	@Override
	public synchronized String getTextExecutionId(DefaultTestExecutionContext test_context)
		{
		int hash = test_context.hashCode();  // we use the hash to ensure references to obsolete contexts are not retained
		String execution_id = _test_execution_ids.get(hash);
		if (execution_id != null)
			return execution_id;

		final String test_id = test_context.getTest().getId();
		Integer suffix = _next_test_suffix.get(test_id);
		if (suffix == null)
			suffix = 1;

		execution_id = test_id + "-" + suffix;
		_next_test_suffix.put(test_id, ++suffix);
		_test_execution_ids.put(hash, execution_id);

		return execution_id;
		}

	private Map<Integer, String> _test_execution_ids = new HashMap<>();
	private Map<String, Integer> _next_test_suffix = new HashMap();

	private MuseTestSuite _suite;
	}

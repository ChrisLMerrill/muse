package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.suite.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestExecutionContext extends BaseExecutionContext implements TestExecutionContext
	{
	public DefaultTestExecutionContext(MuseProject project, MuseTest test)
		{
		this(new ProjectExecutionContext(project), test);
		}

	public DefaultTestExecutionContext(MuseExecutionContext parent_context, MuseTest test)
		{
		super(parent_context, ContextVariableScope.Execution);
		_test = test;
		}

	@Override
	public MuseTest getTest()
		{
		return _test;
		}

	@Override
	public String getTestExecutionId()
		{
		if (_test_id == null)
			{
			TestSuiteExecutionContext suite_context = MuseExecutionContext.findAncestor(this, TestSuiteExecutionContext.class);
			if (suite_context != null)
				_test_id = suite_context.getTextExecutionId(this);
			else
				_test_id = Long.toString(System.currentTimeMillis());
			}
		return _test_id;
		}

	protected final MuseTest _test;
	private String _test_id;
	}

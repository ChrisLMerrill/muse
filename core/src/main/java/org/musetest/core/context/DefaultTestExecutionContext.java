package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.suite.*;

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
		TestSuiteExecutionContext suite_context = MuseExecutionContext.findAncestor(this, TestSuiteExecutionContext.class);
		if (suite_context != null)
			return suite_context.getTextExecutionId(this);
		else
			return _test.getId();
		}

	protected final MuseTest _test;
	}

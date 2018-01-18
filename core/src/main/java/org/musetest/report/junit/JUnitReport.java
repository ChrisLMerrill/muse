package org.musetest.report.junit;

import org.musetest.core.*;
import org.musetest.core.suite.plugin.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class JUnitReport implements TestSuitePlugin
	{
	@Override
	public boolean shouldAddToSuite(MuseExecutionContext context, MuseTestSuite suite, boolean automatic)
		{
		return true;
		}

	@Override
	public boolean addToContext(MuseExecutionContext context, boolean automatic) throws MuseExecutionError
		{
		return true;
		}

	@Override
	public void initialize(MuseExecutionContext context) throws MuseExecutionError
		{
		System.out.println("JUnitReport plugin has been initialized");
		}
	}

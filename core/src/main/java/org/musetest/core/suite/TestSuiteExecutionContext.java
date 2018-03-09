package org.musetest.core.suite;

import org.musetest.core.*;
import org.musetest.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestSuiteExecutionContext extends MuseExecutionContext
	{
	MuseTestSuite getSuite();

	String getTextExecutionId(DefaultTestExecutionContext test_context);
	}



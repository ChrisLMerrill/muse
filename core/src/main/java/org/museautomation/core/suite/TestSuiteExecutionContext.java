package org.museautomation.core.suite;

import org.museautomation.core.*;
import org.museautomation.core.context.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestSuiteExecutionContext extends MuseExecutionContext
	{
	MuseTestSuite getSuite();

	String getTextExecutionId(DefaultTestExecutionContext test_context);
	}



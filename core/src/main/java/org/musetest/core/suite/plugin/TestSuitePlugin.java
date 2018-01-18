package org.musetest.core.suite.plugin;

import org.musetest.core.*;
import org.musetest.core.test.plugin.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestSuitePlugin extends TestPlugin
	{
	boolean shouldAddToSuite(MuseExecutionContext context, MuseTestSuite suite, boolean automatic);
	}
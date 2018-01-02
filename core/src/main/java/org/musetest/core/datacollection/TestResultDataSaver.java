package org.musetest.core.datacollection;

import org.musetest.core.*;
import org.musetest.core.suite.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestResultDataSaver
	{
	boolean save(MuseProject project, MuseTestResult result, TestConfiguration config, MuseExecutionContext context);
	}



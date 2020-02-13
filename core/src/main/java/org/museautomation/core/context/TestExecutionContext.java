package org.museautomation.core.context;

import org.museautomation.core.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestExecutionContext extends MuseExecutionContext
    {
    MuseTest getTest();
    String getTestExecutionId();
    }
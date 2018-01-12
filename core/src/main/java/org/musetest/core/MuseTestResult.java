package org.musetest.core;

import org.musetest.core.events.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

/**
 * The result of running a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestResult
    {
    boolean isPass();

    MuseTestFailureDescription getFailureDescription();

    MuseTest getTest();

    EventLog getLog();

    String getName();

    String getOneLineDescription();

    TestConfiguration getConfiguration();
    void setConfiguration(TestConfiguration config);
    }



package org.musetest.core;

import org.musetest.core.events.*;

/**
 * The result of running a test.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestResult
    {
    MuseTestResultStatus getStatus();

    MuseTest getTest();

    EventLog getLog();
    }



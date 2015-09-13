package org.musetest.core;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuiteResult
    {
    int getSuccessCount();
    int getFailureCount();
    int getErrorCount();

    List<MuseTestResult> getTestResults();
    }


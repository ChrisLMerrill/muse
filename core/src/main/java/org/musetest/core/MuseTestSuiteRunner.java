package org.musetest.core;

import org.musetest.core.suite.plugin.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuiteRunner
    {
    MuseTestSuiteResult execute(MuseProject project, MuseTestSuite suite, List<TestSuitePlugin> plugins);
    void setOutputPath(String path);
    }


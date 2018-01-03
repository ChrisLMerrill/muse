package org.musetest.core;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuiteRunner
    {
    MuseTestSuiteResult execute(MuseProject project, MuseTestSuite suite);
    void setOutputPath(String path);
    }


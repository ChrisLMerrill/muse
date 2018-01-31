package org.musetest.core;

import org.musetest.core.plugins.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuiteRunner
    {
    boolean execute(MuseProject project, MuseTestSuite suite, List<MusePlugin> plugins);
    void setOutputPath(String path);
    }


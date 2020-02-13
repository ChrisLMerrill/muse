package org.museautomation.core;

import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTestSuiteRunner
    {
    boolean execute(MuseProject project, MuseTestSuite suite, List<MusePlugin> plugins);
    void setOutputPath(String path);
    }


package org.museautomation.core;

import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseTaskSuiteRunner
    {
    boolean execute(MuseProject project, MuseTaskSuite suite, List<MusePlugin> plugins);
    void setOutputPath(String path);
    }


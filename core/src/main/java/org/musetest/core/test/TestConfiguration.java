package org.musetest.core.test;

import org.musetest.core.*;
import org.musetest.core.context.*;
import org.musetest.core.test.plugins.*;

import java.util.*;

/**
 * A factory for everything needed by a TestRunner to run a test.
 *
 * This is intended to be serializable (to be sent to remote machines for execution). Thus, all the accessors here
 * are not get/set methods. Get/set methods are reserved for JSON serialization.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestConfiguration
    {
    void withinProject(MuseProject project);
    void addPlugin(TestPlugin plugin);

    MuseTest test();
    String name();
    TestExecutionContext context();
    List<TestPlugin> plugins();
    }

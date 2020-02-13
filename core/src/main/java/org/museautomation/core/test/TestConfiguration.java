package org.museautomation.core.test;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;

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
    void withinContext(MuseExecutionContext parent_context);
    void addPlugin(MusePlugin plugin);

    MuseTest test();
    String name();
    TestExecutionContext context();
    List<MusePlugin> plugins();
    }

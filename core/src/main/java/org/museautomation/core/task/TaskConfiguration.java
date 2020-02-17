package org.museautomation.core.task;

import org.museautomation.core.*;
import org.museautomation.core.context.*;
import org.museautomation.core.plugins.*;

import java.util.*;

/**
 * A factory for everything needed by a TestRunner to run a task.
 *
 * This is intended to be serializable (to be sent to remote machines for execution). Thus, all the accessors here
 * are not get/set methods. Get/set methods are reserved for JSON serialization.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TaskConfiguration
    {
    void withinContext(MuseExecutionContext parent_context);
    void addPlugin(MusePlugin plugin);

    MuseTask task();
    String name();
    TaskExecutionContext context();
    List<MusePlugin> plugins();
    }

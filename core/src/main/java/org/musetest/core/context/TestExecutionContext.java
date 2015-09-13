package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.test.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface TestExecutionContext
    {
    void raiseEvent(MuseEvent event);

    void addEventListener(MuseEventListener listener);
    void removeEventListener(MuseEventListener listener);

    Object getVariable(String name);
    void setVariable(String name, Object value);

    void registerShuttable(Shuttable shuttable);  // shuttables should be shut down during cleanup
    void cleanup();  // cleanup test resources

    /**
     * If the test is executed within the context of a project, this provides access to the project.
     *
     * @return The project. null if not executed within a project context.
     */
    MuseProject getProject();
    }


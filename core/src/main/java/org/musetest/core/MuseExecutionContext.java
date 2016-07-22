package org.musetest.core;

import org.musetest.core.test.*;

/**
 * @author ©2015 Web Performance, Inc
 */
public interface MuseExecutionContext
    {
    /**
     * Send an event to listeners (which typically includes the EventLog).
     */
    void raiseEvent(MuseEvent event);

    void addEventListener(MuseEventListener listener);

    void removeEventListener(MuseEventListener listener);

    /**
     * Register a resource that should be closed/shutdown when the highest-level context (that
     * this StepExecutionContext lives in) ends.
     */
    void registerShuttable(Shuttable shuttable);

    /**
     * If execution occurs within the context of a project, this provides access to the project.
     *
     * @return The project. null if not executed within a project context.
     */
    MuseProject getProject();

    /**
     * ExecutionContexts can be hierarchical. For example, a StepExecutionContext typically exists within a higher
     * context, like a test, to which it defers much of the interface.
     *
     * @return The parent context or null if none.
     */
    MuseExecutionContext getParent();
    }


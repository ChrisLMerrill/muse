package org.musetest.core;

import org.musetest.core.context.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public interface MuseExecutionContext
    {
    /**
     * Send an event to listeners (which typically includes the EventLog).
     */
    void raiseEvent(MuseEvent event);

    void addEventListener(MuseEventListener listener);

    void removeEventListener(MuseEventListener listener);

    EventLog getEventLog();

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

    /**
     * Get the value of a variable in the current variable scope.  If not found in the current scope, search
     * for the value in higher-level scopes.
     *
     * @return the value assigned to the variable name. null if not found.
     */
    Object getVariable(String name);

    /**
     * Set the value of a variable in the current variable scope. This will hide test-wide variables of
     * the same name from access within this scope.
     */
    void setVariable(String name, Object value);

    /**
     * Get the value of a variable only from the specified variable scope.
     *
     * @return the value assigned to the variable name. null if not found.
     */
    Object getVariable(String name, VariableQueryScope scope);

    /**
     * Set the value of a variable in the specified variable scope.
     */
    void setVariable(String name, Object value, ContextVariableScope scope);

    /**
     * Creates a variable using a generated name (that does not already exist) starting with the prefix provided. The
     * value parameter is stored and the generated name is returned.
     */
    String createVariable(String prefix, Object value);

    /**
     * Cleanup any test resources. E.g. Shuttable
     */
    void cleanup();

    /**
     * Adds a plugin to the test.
     */
    void addPlugin(MusePlugin plugin);

    /**
     * Gets the plugins that are applied to this context (but not the parents)
     */
    List<MusePlugin> getPlugins();

    /**
     * Initialize the configured TestPlugins.
     * @throws MuseExecutionError If a plugin fails or if already initialized.
     * @return The number of plugins that failed to initialize successfully.
     */
    int initializePlugins() throws MuseExecutionError;

    @SuppressWarnings("unused")  // convenience for extensions
    static <T extends MuseExecutionContext> T findAncestor(MuseExecutionContext context, Class<T> type)
	    {
	    while (context != null)
		    {
		    if (type.isAssignableFrom(context.getClass()))
		    	return (T) context;
		    context = context.getParent();
		    }
	    return null;
	    }

    ContextVariableScope getVariableScope();
    }
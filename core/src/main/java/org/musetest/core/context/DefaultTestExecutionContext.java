package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class DefaultTestExecutionContext implements TestExecutionContext
    {
    public DefaultTestExecutionContext()
        {
        _project = new SimpleProject();  // a context is useless without a project.
        _test = null; // won't be able to do some things in this case TODO is this ok?
        }

    public DefaultTestExecutionContext(MuseProject project, MuseTest test)
        {
        _project = project;
        _test = test;
        }

    @Override
    public void raiseEvent(MuseEvent event)
        {
        for (MuseEventListener listener : _listeners.toArray(new MuseEventListener[_listeners.size()]))  // note, this is a threadsafe iteration - listeners can unsubscribe during event dispatch
            {
            try
                {
                listener.eventRaised(event);
                }
            catch (Exception e)
                {
                LOG.error("TextExecutionContext.raiseEvent(): Exception while passing event to listeners: ", e);
                }
            }
        }

    @Override
    public void addEventListener(MuseEventListener listener)
        {
        if (!_listeners.contains(listener))
            _listeners.add(listener);
        }

    @Override
    public void removeEventListener(MuseEventListener listener)
        {
        _listeners.remove(listener);
        }

    private Object getTestVariable(String name)
        {
        return _vars.get(name);
        }

    private void setTestVariable(String name, Object value)
        {
        _vars.put(name, value);
        raiseEvent(new SetVariableEvent(name, value, VariableScope.Execution));
        }

    @Override
    public Object getVariable(String name)
        {
        return getTestVariable(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        setTestVariable(name, value);
        }

    @Override
    public Object getVariable(String name, VariableScope scope)
        {
        if (scope.equals(VariableScope.Execution))
            return getTestVariable(name);
        return null;
        }

    @Override
    public void setVariable(String name, Object value, VariableScope scope)
        {
        if (scope.equals(VariableScope.Execution))
            setTestVariable(name, value);
        else
            LOG.error(String.format("Asked to set a variable in the '%s' scope: %s = %s", scope.name(), name, value));
        }

    @Override
    public MuseProject getProject()
        {
        return _project;
        }

    @Override
    public void registerShuttable(Shuttable shuttable)
        {
        _shuttables.add(shuttable);
        }

    @Override
    public void cleanup()
        {
        _shuttables.forEach(Shuttable::shutdown);
        _listeners.clear();
        }

    @Override
    public MuseExecutionContext getParent()
        {
        return null;
        }

    @Override
    public void addInitializer(ContextInitializer initializer) throws MuseExecutionError
        {
        if (_initialized)
            throw new MuseExecutionError("Context has been initialized...too late to add initializers.");
        _initializers.add(initializer);
        }

    @Override
    public void runInitializers() throws MuseExecutionError
        {
        if (_initialized)
            throw new MuseExecutionError("Context has been initialized...can't run it again.");

        for (ContextInitializer initializer : _initializers)
            initializer.initialize(getProject(), this);
        }

    @Override
    public MuseTest getTest()
        {
        return _test;
        }

    private final MuseProject _project;
    private final MuseTest _test;

    private Map<String, Object> _vars = new HashMap<>();
    private List<MuseEventListener> _listeners = new ArrayList<>();
    private List<Shuttable> _shuttables = new ArrayList<>();
    private List<ContextInitializer> _initializers = new ArrayList<>();
    private boolean _initialized = false;

    private final static Logger LOG = LoggerFactory.getLogger(TestExecutionContext.class);
    }



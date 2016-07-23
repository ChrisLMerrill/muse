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
        }

    public DefaultTestExecutionContext(MuseProject project)
        {
        _project = project;
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

    public void setProject(MuseProject project)
        {
        _project = project;
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

    private Map<String, Object> _vars = new HashMap<>();
    private List<MuseEventListener> _listeners = new ArrayList<>();
    private MuseProject _project;
    private List<Shuttable> _shuttables = new ArrayList<>();

    private final static Logger LOG = LoggerFactory.getLogger(TestExecutionContext.class);
    }



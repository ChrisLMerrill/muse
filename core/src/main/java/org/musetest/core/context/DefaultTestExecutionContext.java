package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.project.*;
import org.musetest.core.test.*;
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

    @Override
    public Object getVariable(String name)
        {
        return _vars.get(name);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        _vars.put(name, value);
        raiseEvent(new SetVariableEvent(name, value));
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
        for (Shuttable shuttable : _shuttables)
            shuttable.shutdown();
        _listeners.clear();
        }

    private Map<String, Object> _vars = new HashMap<>();
    private List<MuseEventListener> _listeners = new ArrayList<>();
    private MuseProject _project;
    private List<Shuttable> _shuttables = new ArrayList<>();

    final static Logger LOG = LoggerFactory.getLogger(TestExecutionContext.class);
    }



package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.test.*;
import org.musetest.core.test.plugins.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BaseExecutionContext implements MuseExecutionContext
    {
    public BaseExecutionContext(MuseProject project)
        {
        _project = project;
        }

    /**
     * Send an event to all event listeners.
     *
     * This is not really intended to be threadsafe, but it is intended for re-entrant safety.
     * I.e. Raising a new event (or multiple events) by an EventListener, during the processing of an event,
     * will queue the event and continue to deliver them in the order they were raised.
     */
    @Override
    public void raiseEvent(MuseEvent event)
        {
        // put it into a queue
        _event_queue.add(event);
        if (_events_processing.get())
        	return;

        _events_processing.set(true);
        while (!_event_queue.isEmpty())
	        {
	        MuseEvent queued_event = _event_queue.remove();
	        for (MuseEventListener listener : _listeners.toArray(new MuseEventListener[_listeners.size()]))  // note, this is a threadsafe iteration - listeners can unsubscribe during event dispatch
                {
                try
                    {
                    listener.eventRaised(queued_event);
                    }
                catch (Exception e)
                    {
                    LOG.error("TextExecutionContext.raiseEvent(): Exception while passing event to listeners: ", e);
                    }
                }
	        }
        _events_processing.set(false);
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
        return getVariable(name, VariableScope.Execution);
        }

    @Override
    public void setVariable(String name, Object value)
        {
        setVariable(name, value, VariableScope.Execution);
        }

    @Override
    public Object getVariable(String name, VariableScope scope)
        {
        if (scope.equals(VariableScope.Execution))
            return _vars.get(name);
        return null;
        }

    @Override
    public void setVariable(String name, Object value, VariableScope scope)
        {
        if (scope.equals(VariableScope.Execution))
            {
            _vars.put(name, value);
            raiseEvent(new SetVariableEvent(name, value, VariableScope.Execution));
            }
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
    public void addTestPlugin(TestPlugin plugin)
        {
        if (_plugins_initialized)
            throw new IllegalStateException("Context has been initialized...too late to add plugins.");
        _plugins.add(plugin);
        }

    @Override
    public void initializePlugins() throws MuseExecutionError
        {
        if (_plugins_initialized)
            throw new MuseExecutionError("Context has been initialized...can't run it again.");

        for (TestPlugin plugin : _plugins)
			plugin.initialize(this);
		_plugins_initialized = true;
		}

	public List<DataCollector> getDataCollectors()
		{
		List<DataCollector> data_collectors = new ArrayList<>();
		for (TestPlugin plugin : _plugins)
		if (plugin instanceof DataCollector)
			data_collectors.add((DataCollector)plugin);
		return data_collectors;
		}

	@Override
	public <T extends DataCollector> T getDataCollector(Class<T> type)
		{
		T the_collector = null;
		for (TestPlugin plugin : _plugins)
			{
			if (type.isAssignableFrom(plugin.getClass()))
				{
				if (the_collector != null)
					throw new IllegalArgumentException("Cannot use this method when there are more than one DataCollectors of the desired type");
				the_collector = (T) plugin;
				}
			}
		return the_collector;
		}

	private final MuseProject _project;

    private Map<String, Object> _vars = new HashMap<>();
    protected List<MuseEventListener> _listeners = new ArrayList<>();
    private List<Shuttable> _shuttables = new ArrayList<>();
    private List<TestPlugin> _plugins = new ArrayList<>();
    private boolean _plugins_initialized = false;
    private Queue<MuseEvent> _event_queue = new ConcurrentLinkedQueue<>();
    private AtomicBoolean _events_processing = new AtomicBoolean(false);

    private final static Logger LOG = LoggerFactory.getLogger(TestExecutionContext.class);
    }



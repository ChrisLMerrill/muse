package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.datacollection.*;
import org.musetest.core.events.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class BaseExecutionContext implements MuseExecutionContext
    {
    public BaseExecutionContext(MuseProject project)
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
    public void addInitializer(ContextInitializer initializer)
        {
        if (_initialized)
            throw new IllegalStateException("Context has been initialized...too late to add initializers.");
        _initializers.add(initializer);
        }

    @Override
    public void runInitializers() throws MuseExecutionError
        {
        if (_initialized)
            throw new MuseExecutionError("Context has been initialized...can't run it again.");

        for (ContextInitializer initializer : _initializers)
			initializer.initialize(this);
		_initialized = true;
		}

	public List<DataCollector> getDataCollectors()
		{
		List<DataCollector> data_collectors = new ArrayList<>();
		for (ContextInitializer initializer : _initializers)
		if (initializer instanceof DataCollector)
			data_collectors.add((DataCollector)initializer);
		return data_collectors;
		}

	@Override
	public <T extends DataCollector> T getDataCollector(Class<T> type)
		{
		T the_collector = null;
		for (ContextInitializer initializer : _initializers)
			{
			if (type.isAssignableFrom(initializer.getClass()))
				{
				if (the_collector != null)
					throw new IllegalArgumentException("Cannot use this method when there are more than one DataCollectors of the desired type");
				the_collector = (T) initializer;
				}
			}
		return the_collector;
		}

	private final MuseProject _project;

    private Map<String, Object> _vars = new HashMap<>();
    protected List<MuseEventListener> _listeners = new ArrayList<>();
    private List<Shuttable> _shuttables = new ArrayList<>();
    private List<ContextInitializer> _initializers = new ArrayList<>();
    private boolean _initialized = false;

    private final static Logger LOG = LoggerFactory.getLogger(TestExecutionContext.class);
    }



package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.events.*;
import org.musetest.core.plugins.*;
import org.musetest.core.test.*;
import org.musetest.core.variables.*;
import org.slf4j.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class BaseExecutionContext implements MuseExecutionContext
	{
	protected BaseExecutionContext(MuseProject project, ContextVariableScope scope)
		{
		_parent = null;
		_scope = scope;
		_project = project;
		_var_creator = new AutomaticVariableCreator(name -> getVariable(name) != null);
		}

	protected BaseExecutionContext(MuseExecutionContext parent, ContextVariableScope scope)
		{
		_parent = parent;
		_scope = scope;
		_project = parent.getProject();
		_var_creator = new AutomaticVariableCreator(name -> getVariable(name) != null);
		}

    /**
	 * Send an event to all event listeners.
	 * <p>
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
		return getVariable(name, VariableQueryScope.Any);
		}

	@Override
	public void setVariable(String name, Object value)
		{
		setVariable(name, value, _scope);
		}

	@Override
	public Object getVariable(String name, VariableQueryScope scope)
		{
		if (scope.appliesToScope(_scope))
			{
			Object value = _vars.get(name);
			if (value != null || !(scope.equals(VariableQueryScope.Any) || scope.equals(VariableQueryScope.AnyButLocal)))
				return value;
			}
		if (_parent != null)
			return getParent().getVariable(name, scope.equals(VariableQueryScope.Any) && _scope.equals(ContextVariableScope.Local) ? VariableQueryScope.AnyButLocal : scope);
		return null;
		}

	@Override
	public void setVariable(String name, Object value, ContextVariableScope scope)
		{
		if (scope.equals(_scope))
			{
			_vars.put(name, value);
			raiseEvent(SetVariableEventType.create(name, value, scope));
			return;
			}
		if (_parent != null)
			_parent.setVariable(name, value, scope);
		else
			LOG.error(String.format("Asked to set a variable (%s = %s) in the '%s' scope. This context has scope %s and no parent to delegate to for the requested scope.", name, value, scope.name(), _scope.name()));
		}

	@Override
	public String createVariable(String prefix, Object value)
		{
		String name = _var_creator.createNextName(prefix);
		setVariable(name, value);
		return name;
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
		return _parent;
		}

	@Override
	public void addPlugin(MusePlugin plugin)
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

		for (MusePlugin plugin : _plugins)
			plugin.initialize(this);
		_plugins_initialized = true;
		}

	public List<MusePlugin> getPlugins()
		{
		return _plugins;
		}

	@Override
	public ContextVariableScope getVariableScope()
		{
		return _scope;
		}

	private final MuseExecutionContext _parent;
	private final MuseProject _project;
	private final AutomaticVariableCreator _var_creator;
	private final ContextVariableScope _scope;

	private Map<String, Object> _vars = new HashMap<>();
	protected List<MuseEventListener> _listeners = new ArrayList<>();
	private List<Shuttable> _shuttables = new ArrayList<>();
	private List<MusePlugin> _plugins = new ArrayList<>();
	private boolean _plugins_initialized = false;
	private Queue<MuseEvent> _event_queue = new ConcurrentLinkedQueue<>();
	private AtomicBoolean _events_processing = new AtomicBoolean(false);

	private final static Logger LOG = LoggerFactory.getLogger(TestExecutionContext.class);

	public final static String EXECUTION_ID_VARNAME = "__exeuction_id__";
	}
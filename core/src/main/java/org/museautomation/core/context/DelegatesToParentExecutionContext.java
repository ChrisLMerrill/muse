package org.museautomation.core.context;

import org.museautomation.core.*;
import org.museautomation.core.events.*;
import org.museautomation.core.plugins.*;
import org.museautomation.core.task.*;
import org.museautomation.core.variables.*;
import org.slf4j.*;

import java.util.*;

/**
 * Delegates all behaviors its parent context. Extenders are expected to override some methods as needed.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public abstract class DelegatesToParentExecutionContext implements MuseExecutionContext
	{
	DelegatesToParentExecutionContext(MuseExecutionContext parent, ContextVariableScope scope)
		{
		_parent = parent;
		_scope = scope;
		}

	@Override
	public void raiseEvent(MuseEvent event)
		{
		_parent.raiseEvent(event);
		}

	@Override
	public void addEventListener(MuseEventListener listener)
		{
		_parent.addEventListener(listener);
		}

	@Override
	public void removeEventListener(MuseEventListener listener)
		{
		_parent.removeEventListener(listener);
		}

	@Override
	public EventLog getEventLog()
		{
		return _parent.getEventLog();
		}

	@Override
	public void registerShuttable(Shuttable shuttable)
		{
		_parent.registerShuttable(shuttable);
		}

	@Override
	public MuseProject getProject()
		{
		return _parent.getProject();
		}

	@Override
	public MuseExecutionContext getParent()
		{
		return _parent;
		}

	@Override
	public Object getVariable(String name)
		{
		if (_scope == null)
			return _parent.getVariable(name);
		else
			return getVariable(name, VariableQueryScope.Any);
		}

	@Override
	public void setVariable(String name, Object value)
		{
		if (_scope == null)
			_parent.setVariable(name, value);
		else
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
		return _parent.createVariable(prefix, value);
		}

	@Override
	public void cleanup()
		{
		_parent.cleanup();
		}

	@Override
	public void addPlugin(MusePlugin plugin)
		{
		_parent.addPlugin(plugin);
		}

	@Override
	public List<MusePlugin> getPlugins()
		{
		return _parent.getPlugins();
		}

	@Override
	public int initializePlugins() throws MuseExecutionError
		{
		return _parent.initializePlugins();
		}

	@Override
	public ContextVariableScope getVariableScope()
		{
		return _scope;
		}

	private final MuseExecutionContext _parent;
	private final ContextVariableScope _scope;
	private final Map<String, Object> _vars = new HashMap<>();

	private final static Logger LOG = LoggerFactory.getLogger(DelegatesToParentExecutionContext.class);
	}

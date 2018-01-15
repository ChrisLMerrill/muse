package org.musetest.core.values;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.util.*;
import org.musetest.core.values.events.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class NamedSourcesContainer implements ContainsNamedSources, Serializable
	{
	public NamedSourcesContainer()
		{
		}

	NamedSourcesContainer(ValueSourceConfiguration parent)
		{
		_parent = parent;
		}

	@Override
	public ValueSourceConfiguration getSource(String name)
		{
		if (_source_map == null)
			return null;
		return _source_map.get(name);
		}

	@Override
	public void addSource(String name, ValueSourceConfiguration source)
		{
		if (source == null)
			throw new IllegalArgumentException("Cannot add a named source with a null value");

		if (_source_map == null)
			_source_map = new HashMap<>();
		ValueSourceConfiguration old_source = _source_map.get(name);
		if (!(Objects.equals(source, old_source)))
			{
			if (old_source != null)
				throw new IllegalArgumentException(String.format("source named %s is already present. can't be added.", name));
			_source_map.put(name, source);
			source.addChangeListener(getSubsourceListener());
			notifyListeners(new NamedSourceAddedEvent(this, name, source));
			}
		}

	@Override
	public ValueSourceConfiguration removeSource(String name)
		{
		if (_source_map == null)
			throw new IllegalArgumentException(String.format("Cannot remove sub-source %s, it does not exist.", name));
		final ValueSourceConfiguration removed = _source_map.remove(name);
		removed.removeChangeListener(getSubsourceListener());
		notifyListeners(new NamedSourceRemovedEvent(this, name, removed));
		return removed;
		}

	@Override
	public boolean renameSource(String old_name, String new_name)
		{
		if (_source_map == null)
			throw new IllegalArgumentException(String.format("Cannot rename sub-source %s, it does not exist.", old_name));
		final ValueSourceConfiguration source = _source_map.remove(old_name);
		if (source == null)
			throw new IllegalArgumentException(String.format("Cannot rename sub-source %s, it does not exist.", old_name));
		_source_map.put(new_name, source);
		notifyListeners(new NamedSourceRenamedEvent(this, new_name, old_name, source));
		return true;
		}

	@Override
	public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
		{
		if (_source_map == null)
			throw new IllegalArgumentException(String.format("Cannot replace sub-source %s, it does not exist.", name));
		final ValueSourceConfiguration old_source = _source_map.remove(name);
		if (old_source == null)
			throw new IllegalArgumentException(String.format("Cannot replace sub-source %s, it does not exist.", name));
		old_source.removeChangeListener(getSubsourceListener());
		new_source.addChangeListener(getSubsourceListener());
		_source_map.put(name, new_source);
		notifyListeners(new NamedSourceReplacedEvent(this, name, old_source, new_source));
		return old_source;
		}

	@JsonIgnore
	@Override
	public Set<String> getSourceNames()
		{
		if (_source_map == null)
			return Collections.emptySet();
		return _source_map.keySet();
		}

	@Override
	public void addChangeListener(ChangeEventListener listener)
		{
		if (_listeners == null)
			_listeners = new HashSet<>();
		_listeners.add(listener);
		}

	@Override
	public boolean removeChangeListener(ChangeEventListener listener)
		{
		if (_listeners == null)
			return false;
		return _listeners.remove(listener);
		}

	@JsonIgnore
	@SuppressWarnings({"unused", "WeakerAccess"})  // used by GUI
	public Set<ChangeEventListener> getListeners()
		{
		return new HashSet<>(getListenersInternal());
		}

	private Set<ChangeEventListener> getListenersInternal()
		{
		if (_listeners == null)
			_listeners = new LinkedHashSet<>();
		return _listeners;
		}

	private void notifyListeners(ChangeEvent event)
		{
		for (ChangeEventListener listener : getListeners()) // use the public method - to be sure the set isn't modified while we're working (and get ConcurrentModificationException)
			listener.changeEventRaised(event);
		}

	private synchronized ChangeEventListener getSubsourceListener()
		{
		if (_subsource_change_listener == null)
			_subsource_change_listener = new SubsourceChangeListener();
		return _subsource_change_listener;
		}

	public Map<String, ValueSourceConfiguration> getSourceMap()
		{
		if (_source_map == null)
			return null;
		return Collections.unmodifiableMap(_source_map);
		}

	public void setSourceMap(Map<String, ValueSourceConfiguration> source_map)
		{
		if (_source_map != null)
			throw new IllegalArgumentException("This method only to be used for deserialization. Cannot call again");
		_source_map = source_map;
		_source_map.values().stream().filter(Objects::nonNull).forEach(source -> source.addChangeListener(getSubsourceListener()));
		}

	@Override
	public boolean equals(Object obj)
		{
		if (!(obj instanceof NamedSourcesContainer))
			return false;
		NamedSourcesContainer other = (NamedSourcesContainer) obj;

		Map<String, ValueSourceConfiguration> map = _source_map;
		if (map == null)
			map = Collections.emptyMap();
		Map<String, ValueSourceConfiguration> other_map = other._source_map;
		if (other_map == null)
			other_map = Collections.emptyMap();
		return Objects.equals(map, other_map);
		}

	private ValueSourceConfiguration _parent = null;
	private Map<String, ValueSourceConfiguration> _source_map = null;
	private transient Set<ChangeEventListener> _listeners;
	private transient SubsourceChangeListener _subsource_change_listener;

	private class SubsourceChangeListener extends ValueSourceChangeObserver
		{
		@Override
		public void changeEventRaised(ChangeEvent event)
			{
			if (!(event instanceof ValueSourceChangeEvent))
				return;
			if (_parent == null)
				{
				notifyListeners(event);
				return;
				}

			ValueSourceChangeEvent e = (ValueSourceChangeEvent) event;
			SubsourceModificationEvent mod_event = null;
			ValueSourceConfiguration modified = e.getSource();
			if (_source_map != null)
				{
				for (String key : _source_map.keySet())
					{
					if (_source_map.get(key) == modified)
						mod_event = new SubsourceModificationEvent(_parent, key, e);
					}
				}
			if (mod_event == null)
				LOG.error("Received an event for an unknown subsource -- somebody forget to de-register a listener!");
			else
				notifyListeners(mod_event);
			}
		}

	private final static Logger LOG = LoggerFactory.getLogger(NamedSourcesContainer.class);
	}

package org.museautomation.core.util;

import org.museautomation.core.step.events.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class MetadataContainer implements ContainsMetadata, Serializable
	{
	@SuppressWarnings("unused")
	public MetadataContainer()
		{
		_owner = this;
		}

	public MetadataContainer(ContainsMetadata owner, ChangeEventListener relay)
		{
		_owner = owner;
		_relay = relay;
		}

	@Override
	public void setMetadataField(String name, Object value)
		{
		if (_map == null)
			_map = new HashMap<>();
		Object old_value = _map.get(name);
		_map.put(name, value);
		if (_map.size() == 0)
			_map = null;

		notifyListeners(new MetadataChangeEvent(_owner, name, old_value, value));
		}

	@Override
	public void removeMetadataField(String name)
		{
		if (_map != null)
			{
			Object old_value = _map.remove(name);
			if (_map.size() == 0)
				_map = null;
			notifyListeners(new MetadataChangeEvent(_owner, name, old_value, null));
			}
		}

	@Override
	public Object getMetadataField(String name)
		{
		if (_map == null)
			return null;
		return _map.get(name);
		}

	@Override
	public Set<String> getMetadataFieldNames()
		{
		if (_map == null)
			return Collections.emptySet();
		return _map.keySet();
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
			_listeners = new HashSet<>();
		return _listeners.remove(listener);
		}

	public Map<String, Object> getMap()
		{
		return _map;
		}

	public void setMap(Map<String, Object> map)
		{
		_map = map;
		}

	private void notifyListeners(MetadataChangeEvent event)
		{
		if (_relay != null)
			_relay.changeEventRaised(event);
		if (_listeners != null)
			for (ChangeEventListener listener : _listeners)
				listener.changeEventRaised(event);
		}

	private Map<String, Object> _map = null;
	final private ContainsMetadata _owner;
	private transient Set<ChangeEventListener> _listeners;
	private transient ChangeEventListener _relay = null;
	}
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
public class IndexedSourcesContainer implements ContainsIndexedSources, Serializable
	{
	public IndexedSourcesContainer()
		{
		}

	@SuppressWarnings("WeakerAccess")  // used in GUI
	public IndexedSourcesContainer(ValueSourceConfiguration parent)
		{
		_parent = parent;
		}

	@Override
	public void addSource(ValueSourceConfiguration source)
		{
		if (source == null)
			throw new IllegalArgumentException("Null sources not allowed");
		if (_source_list == null)
			_source_list = new ArrayList<>();
		int index = _source_list.size();
		addSource(index, source);
		}

	@Override
	public void addSource(int index, ValueSourceConfiguration source)
		{
		if (source == null)
			throw new IllegalArgumentException("Null sources not allowed");
		if (_source_list == null)
			_source_list = new ArrayList<>();

		_source_list.add(index, source);
		source.addChangeListener(getSubsourceListener());
		notifyListeners(new IndexedSourceAddedEvent(this, index, source));
		}

	@Override
	public ValueSourceConfiguration getSource(int index)
		{
		if (_source_list == null || index < 0 || index >= _source_list.size())
			return null;

		return _source_list.get(index);
		}

	@Override
	public ValueSourceConfiguration removeSource(int index)
		{
		if (_source_list == null || index < 0 || index >= _source_list.size())
			return null;

		ValueSourceConfiguration removed = _source_list.remove(index);
		if (_source_list.size() == 0)
			_source_list = null;

		if (removed != null)
			{
			removed.removeChangeListener(getSubsourceListener());
			notifyListeners(new IndexedSourceRemovedEvent(this, index, removed));
			}

		return removed;
		}

	@Override
	public ValueSourceConfiguration replaceSource(int index, ValueSourceConfiguration new_source)
		{
		if (new_source == null)
			throw new IllegalArgumentException("Null sources not allowed");

		if (_source_list == null || index < 0 || index >= _source_list.size())
			throw new IllegalArgumentException(String.format("Source %d does not exist", index));

		ValueSourceConfiguration old_source = _source_list.set(index, new_source);
		old_source.removeChangeListener(getSubsourceListener());
		new_source.addChangeListener(getSubsourceListener());
		notifyListeners(new IndexedSourceReplacedEvent(this, index, old_source, new_source));
		return old_source;
		}

	public List<ValueSourceConfiguration> getSourceList()
		{
		return _source_list;
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

	public void setSourceList(List<ValueSourceConfiguration> new_list)
		{
		if (_source_list != null)
			for (ValueSourceConfiguration source : _source_list)
				source.removeChangeListener(getSubsourceListener());
		_source_list = new_list;
		if (_source_list != null)
			for (ValueSourceConfiguration source : _source_list)
				source.addChangeListener(getSubsourceListener());
		}

	private ValueSourceConfiguration _parent;
	private List<ValueSourceConfiguration> _source_list = new ArrayList<>();
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
			if (_source_list != null)
				{
				for (int i = 0; i < _source_list.size(); i++)
					{
					ValueSourceConfiguration source = _source_list.get(i);
					if (source == modified)
						mod_event = new SubsourceModificationEvent(_parent, i, e);
					}
				}
			if (mod_event == null)
				LOG.error("Received an event for an unknown subsource -- somebody forget to de-register a listener!");
			else
				notifyListeners(mod_event);
			}
		}

	private final static Logger LOG = LoggerFactory.getLogger(IndexedSourcesContainer.class);
	}

package org.musetest.core.values;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.events.*;
import org.musetest.core.values.factory.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill, Copyright 2015 (see LICENSE.txt for license details)
 */
public class ValueSourceConfiguration implements Serializable
    {
    public ValueSourceConfiguration()
        {
        }

    /**
     * This methods should be used only for unit tests - as it will create the MuseValueSource as a part of a SimpleProject.
     */
    public MuseValueSource createSource() throws MuseInstantiationException
        {
        return createSource(new SimpleProject());
        }

    public MuseValueSource createSource(MuseProject project) throws MuseInstantiationException
        {
        return ValueSourceFactory.getDefault(project).createSource(this, project);
        }

    public String getType()
        {
        return _type;
        }

    public void setType(String type)
        {
        if (!Objects.equals(type, _type))
            {
            String old_type = _type;
            _type = type;
            notifyListeners(new TypeChangeEvent(this, _type, old_type));
            }
        }

    public Object getValue()
        {
        return _value;
        }

    public void setValue(Object value)
        {
        if (!Objects.equals(value, _value))
            {
            Object old_value = _value;
            _value = value;
            notifyListeners(new ValueChangeEvent(this, value, old_value));
            }
        }

    public ValueSourceConfiguration getSource()
        {
        return _source;
        }

    public void setSource(ValueSourceConfiguration source)
        {
        if (!Objects.equals(_source, source))
            {
            ValueSourceConfiguration old_source = _source;
            _source = source;

            if (old_source != null)
                old_source.removeChangeListener(getSubsourceListener());
            if (source != null)
                source.addChangeListener(getSubsourceListener());
            notifyListeners(new SingularSubsourceChangeEvent(this, source, old_source));
            }
        }

    /**
     * required for serialization. Should not be used externally
     *
     * @return A map of named value sources contained in this source.
     */
    public Map<String, ValueSourceConfiguration> getSourceMap()
        {
        return _source_map;
        }

    @JsonIgnore
    @SuppressWarnings("unused") // used by GUI
    public Set<String> getSourceNames()
        {
        if (_source_map == null)
            return Collections.emptySet();
        return _source_map.keySet();
        }

    public void setSourceMap(Map<String, ValueSourceConfiguration> source_map)
        {
        if (_source_map != null)
            for (ValueSourceConfiguration source : _source_map.values())
                source.removeChangeListener(getSubsourceListener());
        _source_map = source_map;
        if (_source_map != null)
            for (ValueSourceConfiguration source : _source_map.values())
                source.addChangeListener(getSubsourceListener());
        }

    public List<ValueSourceConfiguration> getSourceList()
        {
        return _source_list;
        }

    public void setSourceList(List<ValueSourceConfiguration> source_list)
        {
        if (_source_list != null)
            for (ValueSourceConfiguration source : _source_list)
                source.removeChangeListener(getSubsourceListener());
        _source_list = source_list;
        if (_source_list != null)
            for (ValueSourceConfiguration source : _source_list)
                source.addChangeListener(getSubsourceListener());
        }

    public static ValueSourceConfiguration fromString(String string) throws IOException
        {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ByteArrayInputStream(string.getBytes()), ValueSourceConfiguration.class);
        }

    public void addSource(ValueSourceConfiguration source)
        {
        if (_source_list == null)
            _source_list = new ArrayList<>();
        int index = _source_list.size();
        addSource(index, source);
        }

    public void addSource(String name, ValueSourceConfiguration source)
        {
        if (_source_map == null)
            _source_map = new HashMap<>();
        ValueSourceConfiguration old_source = _source_map.get(name);
        if (!(Objects.equals(source, old_source)))
            {
            _source_map.put(name, source);
            if (old_source != null)
                old_source.removeChangeListener(getSubsourceListener());
            if (source != null)
                source.addChangeListener(getSubsourceListener());
            notifyListeners(new NamedSourceAddedEvent(this, name, source));
            }
        }

    public void addSource(int index, ValueSourceConfiguration source)
        {
        if (_source_list == null)
            _source_list = new ArrayList<>();

        _source_list.add(index, source);
        source.addChangeListener(getSubsourceListener());
        notifyListeners(new IndexedSourceAddedEvent(this, index, source));
        }

    public ValueSourceConfiguration getSource(String name)
        {
        if (_source_map == null)
            return null;
        return _source_map.get(name);
        }

    public ValueSourceConfiguration getSource(int index)
        {
        if (_source_list == null)
            return null;
        return _source_list.get(index);
        }

    public ValueSourceConfiguration removeSource(String name)
        {
        if (_source_map == null)
            return null;

        ValueSourceConfiguration removed = _source_map.remove(name);
        if (removed != null)
            {
            removed.removeChangeListener(getSubsourceListener());
            notifyListeners(new NamedSourceRemovedEvent(this, name, removed));
            }
        return removed;
        }

    public ValueSourceConfiguration removeSource(int index)
        {
        if (_source_list == null)
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

    public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
        {
        ValueSourceConfiguration old_source = _source_map.remove(name);
        if (old_source == null)
            throw new IllegalArgumentException(String.format("Cannot replace sub-source %s, it does not exist.", name));
        _source_map.put(name, new_source);
        old_source.removeChangeListener(getSubsourceListener());
        if (new_source != null)
            new_source.addChangeListener(getSubsourceListener());
        notifyListeners(new NamedSourceReplacedEvent(this, name, old_source, new_source));
        return old_source;
        }

    public ValueSourceConfiguration replaceSource(int index, ValueSourceConfiguration new_source)
        {
        ValueSourceConfiguration old_source = _source_list.set(index, new_source);
        if (old_source == null)
            throw new IllegalArgumentException(String.format("Cannot replace sub-source %d, it does not exist.", index));
        old_source.removeChangeListener(getSubsourceListener());
        if (new_source != null)
            new_source.addChangeListener(getSubsourceListener());
        notifyListeners(new IndexedSourceReplacedEvent(this, index, old_source, new_source));
        return old_source;
        }

    public boolean renameSource(String old_name, String new_name)
        {
        ValueSourceConfiguration source = _source_map.remove(old_name);
        if (source == null)
            return false;
        _source_map.put(new_name, source);
        notifyListeners(new NamedSourceRenamedEvent(this, new_name, old_name, source));
        return true;
        }

    @Override
    public boolean equals(Object obj)
        {
        if (!(obj instanceof ValueSourceConfiguration))
            return false;
        ValueSourceConfiguration other = (ValueSourceConfiguration) obj;

        if (!_type.equals(other._type))
            return false;

        if (!Objects.equals(_value, other._value))
            return false;

        if (!Objects.equals(_source, other._source))
            return false;

        // a little extra work, because a null reference to a list is technically not the same as an empty list (but for our purposes, it is)
        List<ValueSourceConfiguration> list = _source_list;
        if (list == null)
            list = Collections.emptyList();
        List<ValueSourceConfiguration> other_list = other._source_list;
        if (other_list == null)
            other_list = Collections.emptyList();
        if (!Objects.equals(list, other_list))
            return false;

        // a little extra work, because a null reference to a map is technically not the same as an empty map (but for our purposes, it is)
        Map<String, ValueSourceConfiguration> map = _source_map;
        if (map == null)
            map = Collections.emptyMap();
        Map<String, ValueSourceConfiguration> other_map = other._source_map;
        if (other_map == null)
            other_map = Collections.emptyMap();
        if (!Objects.equals(map, other_map))
            return false;

        return true;
        }

    public void addChangeListener(ValueSourceChangeListener listener)
        {
        getListenersInternal().add(listener);
        }

    public boolean removeChangeListener(ValueSourceChangeListener listener)
        {
        return getListenersInternal().remove(listener);
        }

    private void notifyListeners(ValueSourceChangeEvent event)
        {
        for (ValueSourceChangeListener listener : getListeners()) // use the public method - to be sure the set isn't modified while we're working (and get ConcurrentModificationException)
            listener.changed(event);
        }

    @JsonIgnore
    @SuppressWarnings({"unused", "WeakerAccess"})  // used by GUI
    public Set<ValueSourceChangeListener> getListeners()
        {
        HashSet<ValueSourceChangeListener> new_set = new HashSet<>();
        new_set.addAll(getListenersInternal());
        return new_set;
        }

    private Set<ValueSourceChangeListener> getListenersInternal()
        {
        if (_listeners == null)
            _listeners = new LinkedHashSet<>();
        return _listeners;
        }

    private synchronized ValueSourceChangeListener getSubsourceListener()
        {
        if (_listener == null)
            _listener = new SubsourceChangeListener();
        return _listener;
        }

    @Override
    public String toString()
        {
        StringBuilder builder = new StringBuilder(_type);
        builder.append("(");
        boolean first = true;
        if (_value != null)
            {
            builder.append("value=");
            builder.append(_value.toString());
            first = false;
            }
        if (_source != null)
            {
            if (!first)
                builder.append(",");
            builder.append("source=");
            builder.append(_source.toString());
            first = false;
            }
        if (_source_list != null && _source_list.size() > 0)
            {
            if (!first)
                builder.append(",");
            builder.append("list=[");
            boolean first_in_list = true;
            for (ValueSourceConfiguration source : _source_list)
                {
                if (!first_in_list)
                    builder.append(",");
                builder.append(source.toString());
                first_in_list = false;
                }
            builder.append("]");
            first = false;
            }
        if (_source_map != null && _source_map.size() > 0)
            {
            if (!first)
                builder.append(",");
            builder.append("map={");
            boolean first_in_list = true;
            for (String name : _source_map.keySet())
                {
                ValueSourceConfiguration source = _source_map.get(name);
                if (!first_in_list)
                    builder.append(",");
                builder.append(name);
                builder.append("=");
                builder.append(source.toString());
                first_in_list = false;
                }
            builder.append("}");
            }
        builder.append(")");
        return builder.toString();
        }

    private Object _value;
    private ValueSourceConfiguration _source;
    private String _type;
    private Map<String, ValueSourceConfiguration> _source_map;
    private List<ValueSourceConfiguration> _source_list;

    private transient Set<ValueSourceChangeListener> _listeners;
    private transient ValueSourceChangeListener _listener;

    private class SubsourceChangeListener extends ValueSourceChangeObserver
        {
        @Override
        public void changed(ValueSourceChangeEvent event)
            {
            SubsourceModificationEvent mod_event = null;
            ValueSourceConfiguration modified = event.getSource();
            if (modified == _source)
                mod_event = new SubsourceModificationEvent(ValueSourceConfiguration.this, event);
            else if (_source_list != null && _source_list.contains(modified))
                mod_event = new SubsourceModificationEvent(ValueSourceConfiguration.this, _source_list.indexOf(modified), event);
            else if (_source_map != null)
                {
                for (String key : _source_map.keySet())
                    {
                    if (_source_map.get(key) == modified)
                        mod_event = new SubsourceModificationEvent(ValueSourceConfiguration.this, key, event);
                    }
                }
            if (mod_event == null)
                LOG.error("Received an event for an unknown subsource -- somebody forget to de-register a listener!");
            else
                notifyListeners(mod_event);
            }
        }

    //
    // convenient factory methods for unit tests
    //

    public static ValueSourceConfiguration forType(String type)
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType(type);
        return config;
        }

    public static ValueSourceConfiguration forTypeWithSource(String type, Object value)
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType(type);
        ValueSourceConfiguration subsource;
        if (value instanceof  ValueSourceConfiguration)
            subsource = (ValueSourceConfiguration) value;
        else
            subsource = ValueSourceConfiguration.forValue(value);
        config.setSource(subsource);
        return config;
        }

    public static ValueSourceConfiguration forTypeWithIndexedSource(String type, Object value)
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType(type);
        ValueSourceConfiguration subsource;
        if (value instanceof  ValueSourceConfiguration)
            subsource = (ValueSourceConfiguration) value;
        else
            subsource = ValueSourceConfiguration.forValue(value);
        config.addSource(0, subsource);
        return config;
        }

    @SuppressWarnings("unused")  // used in GUI
    public static ValueSourceConfiguration forTypeWithNamedSource(String type, String name, Object value)
        {
        ValueSourceConfiguration config = ValueSourceConfiguration.forType(type);
        ValueSourceConfiguration subsource;
        if (value instanceof ValueSourceConfiguration)
            subsource = (ValueSourceConfiguration) value;
        else
            subsource = ValueSourceConfiguration.forValue(value);
        config.addSource(name, subsource);
        return config;
        }

    public static ValueSourceConfiguration forSource(String type, ValueSourceConfiguration source)
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType(type);
        config.setSource(source);
        return config;
        }

    public static ValueSourceConfiguration forValue(Object value)
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        if (value == null)
            config.setType(NullValueSource.TYPE_ID);
        else if (value instanceof Boolean)
            config.setType(BooleanValueSource.TYPE_ID);
        else if (value instanceof Long)
            config.setType(IntegerValueSource.TYPE_ID);
        else
            config.setType(StringValueSource.TYPE_ID);
        config.setValue(value);
        return config;
        }

    private final static Logger LOG = LoggerFactory.getLogger(ValueSourceConfiguration.class);
    }

package org.musetest.core.values;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.values.factory.*;

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
        String old_type = _type;
        _type = type;

        if (!Objects.equals(type, old_type))
            for (VSCObserver observer : getObservers())
                observer.typeChanged(old_type, type);
        }

    public Object getValue()
        {
        return _value;
        }

    public void setValue(Object value)
        {
        Object old_value = _value;
        _value = value;

        if (!Objects.equals(old_value, value))
            for (VSCObserver observer : getObservers())
                observer.valueChanged(old_value, value);
        }

    public ValueSourceConfiguration getSource()
        {
        return _source;
        }

    public void setSource(ValueSourceConfiguration source)
        {
        ValueSourceConfiguration old_source = _source;
        _source = source;

        if (!Objects.equals(old_source, source))
            for (VSCObserver observer : getObservers())
                observer.sourceChanged(old_source, source);
        }

    /**
     * required for serialization. Should not be used externally
     */
    public Map<String, ValueSourceConfiguration> getSourceMap()
        {
        return _source_map;
        }

    @JsonIgnore
    public Set<String> getSourceNames()
        {
        if (_source_map == null)
            return Collections.emptySet();
        return _source_map.keySet();
        }

    public void setSourceMap(Map<String, ValueSourceConfiguration> source_map)
        {
        Map<String, ValueSourceConfiguration> old_map = _source_map;
        _source_map = source_map;

        if (!Objects.equals(old_map, source_map))
            for (VSCObserver observer : getObservers())
                observer.mapChanged(old_map, source_map);
        }

    public List<ValueSourceConfiguration> getSourceList()
        {
        return _source_list;
        }

    public void setSourceList(List<ValueSourceConfiguration> source_list)
        {
        List<ValueSourceConfiguration> old_list = _source_list;
        _source_list = source_list;

        if (!Objects.equals(old_list, source_list))
            for (VSCObserver observer : getObservers())
                observer.listChanged(old_list, source_list);
        }

    public static ValueSourceConfiguration fromString(String string) throws IOException
        {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ByteArrayInputStream(string.getBytes()), ValueSourceConfiguration.class);
        }

    public void addSource(ValueSourceConfiguration configuration)
        {
        if (_source_list == null)
            _source_list = new ArrayList<>();
        int index = _source_list.size();
        _source_list.add(configuration);
        for (VSCObserver observer : getObservers())
            observer.sourceAddedToList(index, configuration);
        }

    public void addSource(String name, ValueSourceConfiguration source)
        {
        if (_source_map == null)
            _source_map = new HashMap<>();
        _source_map.put(name, source);
        for (VSCObserver observer : getObservers())
            observer.sourceAddedToMap(name, source);
        }

    public void addSource(int index, ValueSourceConfiguration source)
        {
        if (_source_list == null)
            _source_list = new ArrayList<>();
        _source_list.add(index, source);
        for (VSCObserver observer : getObservers())
            observer.sourceAddedToList(index, source);
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
            for (VSCObserver observer : getObservers())
                observer.sourceRemoved(name, removed);
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
            for (VSCObserver observer : getObservers())
                observer.sourceRemoved(index, removed);

        return removed;
        }

    public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
        {
        ValueSourceConfiguration old_source = _source_map.remove(name);
        if (old_source == null)
            throw new IllegalArgumentException(String.format("Cannot replace sub-source %s, it does not exist.", name));
        _source_map.put(name, new_source);
        for (VSCObserver observer : getObservers())
            observer.sourceReplaced(name, old_source, new_source);
        return old_source;
        }

    public ValueSourceConfiguration replaceSource(int index, ValueSourceConfiguration new_source)
        {
        ValueSourceConfiguration old_source = _source_list.set(index, new_source);
        if (old_source == null)
            throw new IllegalArgumentException(String.format("Cannot replace sub-source %d, it does not exist.", index));
        for (VSCObserver observer : getObservers())
            observer.sourceReplaced(index, old_source, new_source);
        return old_source;
        }

    public boolean renameSource(String old_name, String new_name)
        {
        ValueSourceConfiguration source = _source_map.remove(old_name);
        if (source == null)
            return false;
        _source_map.put(new_name, source);
        for (VSCObserver observer : getObservers())
            observer.sourceRenamed(old_name, new_name);
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

    Object _value;
    ValueSourceConfiguration _source;
    String _type;
    Map<String, ValueSourceConfiguration> _source_map;
    List<ValueSourceConfiguration> _source_list;

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
        config.setSource(ValueSourceConfiguration.forValue(value));
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
        if (value instanceof Boolean)
            config.setType(BooleanValueSource.TYPE_ID);
        else if (value instanceof Long)
            config.setType(IntegerValueSource.TYPE_ID);
        else
            config.setType(StringValueSource.TYPE_ID);
        config.setValue(value);
        return config;
        }

    public void addVSCObserver(VSCObserver observer)
        {
        if (_observers == null)
            _observers = new ArrayList<>();
        _observers.add(observer);
        }

    public void removeVSCObserver(VSCObserver observer)
        {
        if (_observers != null)
            _observers.remove(observer);
        }

    private List<VSCObserver> getObservers()
        {
        if (_observers == null)
            _observers = new ArrayList<>();
        return _observers;
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

    private transient List<VSCObserver> _observers;
    }



package org.musetest.core.values;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.util.*;
import org.musetest.core.values.events.*;
import org.musetest.core.values.factory.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill, Copyright 2015 (see LICENSE.txt for license details)
 */
public class ValueSourceConfiguration implements Serializable, ContainsNamedSources
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
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Map<String, ValueSourceConfiguration> getSourceMap()
        {
        return _named_sources.getSourceMap();    // delegate
        }

    @JsonIgnore
    @SuppressWarnings("unused") // used by GUI
    public Set<String> getSourceNames()
        {
        return _named_sources.getSourceNames();  // delegate
        }

    /**
     * required for serialization. Should not be used externally
     */
    public void setSourceMap(Map<String, ValueSourceConfiguration> source_map)
        {
        _named_sources.setSourceMap(source_map);
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
        _named_sources.addSource(name, source);  // delegate
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
        return _named_sources.getSource(name);  // delegate
        }

    public ValueSourceConfiguration getSource(int index)
        {
        if (_source_list == null)
            return null;
        return _source_list.get(index);
        }

    public ValueSourceConfiguration removeSource(String name)
        {
        return _named_sources.removeSource(name);  // delegate
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
        return _named_sources.replaceSource(name, new_source);  // delegate
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
        return _named_sources.renameSource(old_name, new_name);  // delegate
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

        return Objects.equals(_named_sources, other._named_sources);
        }

    public void addChangeListener(ChangeEventListener listener)
        {
        getListenersInternal().add(listener);
        _named_sources.addChangeListener(listener);
        }

    public boolean removeChangeListener(ChangeEventListener listener)
        {
        _named_sources.addChangeListener(listener);
        return getListenersInternal().remove(listener);
        }

    private void notifyListeners(ChangeEvent event)
        {
        for (ChangeEventListener listener : getListeners()) // use the public method - to be sure the set isn't modified while we're working (and get ConcurrentModificationException)
            listener.changeEventRaised(event);
        }

    @JsonIgnore
    @SuppressWarnings({"unused", "WeakerAccess"})  // used by GUI
    public Set<ChangeEventListener> getListeners()
        {
        HashSet<ChangeEventListener> new_set = new HashSet<>();
        new_set.addAll(getListenersInternal());
        return new_set;
        }

    private Set<ChangeEventListener> getListenersInternal()
        {
        if (_listeners == null)
            _listeners = new LinkedHashSet<>();
        return _listeners;
        }

    private synchronized ChangeEventListener getSubsourceListener()
        {
        if (_subsource_change_listener == null)
            _subsource_change_listener = new SubsourceChangeListener();
        return _subsource_change_listener;
        }

    @JsonIgnore
    @SuppressWarnings("unused")  // used in UI
    public void setMetadataField(String name, Object value)
        {
        if (_metadata == null)
            _metadata = new HashMap<>();
        _metadata.put(name, value);
        if (_metadata.size() == 0)
            _metadata = null;
        }

    @JsonIgnore
    @SuppressWarnings("unused")  // used in UI
    public Object getMetadataField(String name)
        {
        if (_metadata == null)
            return null;
        return _metadata.get(name);
        }

    @SuppressWarnings("unused")  // required for JSON de/serialization
    public Map<String, Object> getMetadata()
        {
        return _metadata;
        }

    @SuppressWarnings("unused")  // required for JSON de/serialization
    public void setMetadata(Map<String, Object> metadata)
        {
        _metadata = metadata;
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
        if (_named_sources != null && _named_sources.getSourceNames().size() > 0)
            {
            if (!first)
                builder.append(",");
            builder.append("map={");
            boolean first_in_list = true;
            for (String name : _named_sources.getSourceNames())
                {
                ValueSourceConfiguration source = _named_sources.getSource(name);
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
//    private Map<String, ValueSourceConfiguration> _source_map;
    private NamedSourcesContainer _named_sources = new NamedSourcesContainer(this);
    private List<ValueSourceConfiguration> _source_list;
    private Map<String, Object> _metadata = null;


    private transient Set<ChangeEventListener> _listeners;
    private transient ChangeEventListener _subsource_change_listener;

    private class SubsourceChangeListener extends ValueSourceChangeObserver
        {
        @Override
        public void changeEventRaised(ChangeEvent event)
            {
            if (! (event instanceof ValueSourceChangeEvent))
                return;
            ValueSourceChangeEvent e = (ValueSourceChangeEvent) event;
            SubsourceModificationEvent mod_event = null;
            ValueSourceConfiguration modified = e.getSource();
            if (modified == _source)
                mod_event = new SubsourceModificationEvent(ValueSourceConfiguration.this, e);
            else if (_source_list != null && _source_list.contains(modified))
                mod_event = new SubsourceModificationEvent(ValueSourceConfiguration.this, _source_list.indexOf(modified), e);
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
        if (value instanceof ValueSourceConfiguration)
            subsource = (ValueSourceConfiguration) value;
        else
            subsource = ValueSourceConfiguration.forValue(value);
        config.setSource(subsource);
        return config;
        }

    public static ValueSourceConfiguration forTypeWithValue(String type, Object value)
        {
        ValueSourceConfiguration config = new ValueSourceConfiguration();
        config.setType(type);
        config.setValue(value);
        return config;
        }

    @SuppressWarnings("unused")  // used in GUI tests
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
        else if (value instanceof Integer)
	        {
	        config.setType(IntegerValueSource.TYPE_ID);
	        value = new Long((Integer)value);
	        }
        else
            config.setType(StringValueSource.TYPE_ID);
        config.setValue(value);
        return config;
        }

    private final static Logger LOG = LoggerFactory.getLogger(ValueSourceConfiguration.class);
    }

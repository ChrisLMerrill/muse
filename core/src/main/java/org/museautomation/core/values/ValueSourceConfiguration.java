package org.museautomation.core.values;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.*;
import org.museautomation.builtins.value.*;
import org.museautomation.builtins.value.collection.*;
import org.museautomation.core.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.util.*;
import org.museautomation.core.values.events.*;
import org.museautomation.core.values.factory.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill, Copyright 2015 (see LICENSE.txt for license details)
 */
public class ValueSourceConfiguration implements Serializable, ContainsNamedSources, ContainsIndexedSources
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
        return _indexed_sources.getSourceList();
        }

    public void setSourceList(List<ValueSourceConfiguration> source_list)
        {
        _indexed_sources.setSourceList(source_list);
        }

    public static ValueSourceConfiguration fromString(String string) throws IOException
        {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new ByteArrayInputStream(string.getBytes()), ValueSourceConfiguration.class);
        }

    public void addSource(ValueSourceConfiguration source)
        {
        _indexed_sources.addSource(source);
        }

    public void addSource(String name, ValueSourceConfiguration source)
        {
        _named_sources.addSource(name, source);  // delegate
        }

    public void addSource(int index, ValueSourceConfiguration source)
        {
        _indexed_sources.addSource(index, source);
        }

    public ValueSourceConfiguration getSource(String name)
        {
        return _named_sources.getSource(name);  // delegate
        }

    public ValueSourceConfiguration getSource(int index)
        {
        return _indexed_sources.getSource(index);
        }

    public ValueSourceConfiguration removeSource(String name)
        {
        return _named_sources.removeSource(name);  // delegate
        }

    public ValueSourceConfiguration removeSource(int index)
        {
        return _indexed_sources.removeSource(index);
        }

    public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
        {
        return _named_sources.replaceSource(name, new_source);  // delegate
        }

    public ValueSourceConfiguration replaceSource(int index, ValueSourceConfiguration new_source)
        {
        return _indexed_sources.replaceSource(index, new_source);
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
        List<ValueSourceConfiguration> list = _indexed_sources.getSourceList();
        if (list == null)
            list = Collections.emptyList();
        List<ValueSourceConfiguration> other_list = other.getSourceList();
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
        _indexed_sources.addChangeListener(listener);
        }

    public boolean removeChangeListener(ChangeEventListener listener)
        {
        _named_sources.removeChangeListener(listener);
        _indexed_sources.removeChangeListener(listener);
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
        return new HashSet<>(getListenersInternal());
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
        if (_indexed_sources.getSourceList() != null && _indexed_sources.getSourceList().size() > 0)
            {
            if (!first)
                builder.append(",");
            builder.append("list=[");
            boolean first_in_list = true;
            for (ValueSourceConfiguration source : _indexed_sources.getSourceList())
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
    private NamedSourcesContainer _named_sources = new NamedSourcesContainer(this);
    private IndexedSourcesContainer _indexed_sources = new IndexedSourcesContainer(this);
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
	        value = ((Integer) value).longValue();
	        }
        else if (value instanceof List)
	        {
	        config.setType(ListSource.TYPE_ID);
	        List list = (List) value;
            for (int i = 0; i <list.size(); i++)
                config.addSource(i, forValue(list.get(i)));
	        return config;
	        }
        else
            {
            config.setType(StringValueSource.TYPE_ID);
            value = value.toString();
            }
        config.setValue(value);
        return config;
        }

    public static ValueSourceConfiguration parsePrimitive(String string)
	    {
	    switch (string.toLowerCase())
		    {
		    case "null":
			    return ValueSourceConfiguration.forValue(null);
		    case "true":
			    return ValueSourceConfiguration.forValue(true);
		    case "false":
			    return ValueSourceConfiguration.forValue(false);
		    }
	    try
		    {
		    return ValueSourceConfiguration.forValue(Long.parseLong(string));
		    }
	    catch (NumberFormatException e)
		    {
		    // ok
		    }
		return ValueSourceConfiguration.forValue(string);
	    }

    private final static Logger LOG = LoggerFactory.getLogger(ValueSourceConfiguration.class);
    }

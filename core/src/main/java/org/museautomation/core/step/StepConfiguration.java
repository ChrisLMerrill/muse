package org.museautomation.core.step;

import com.fasterxml.jackson.annotation.*;
import org.museautomation.builtins.value.*;
import org.museautomation.core.*;
import org.museautomation.core.metadata.*;
import org.museautomation.core.project.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.step.descriptor.*;
import org.museautomation.core.step.events.*;
import org.museautomation.core.step.events.TypeChangeEvent;
import org.museautomation.core.step.factory.*;
import org.museautomation.core.util.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.descriptor.*;
import org.museautomation.core.values.events.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepConfiguration implements Serializable, ContainsNamedSources, Taggable, ContainsMetadata
	{
	public StepConfiguration()
		{
		}

	public StepConfiguration(String step_type)
		{
		_step_type = step_type;
		}

	public StepConfiguration(StepDescriptor descriptor)
		{
		_step_type = descriptor.getType();
		for (SubsourceDescriptor source_descriptor : descriptor.getSubsourceDescriptors())
			if (!source_descriptor.isOptional())
				addSource(source_descriptor.getName(), ValueSourceConfiguration.forType(StringValueSource.TYPE_ID));
		}

	public String getType()
		{
		return _step_type;
		}

	public void setType(String step_type)
		{
		if (step_type.equals(_step_type))
			return;

		String old_type = _step_type;
		_step_type = step_type;
		notifyListeners(new TypeChangeEvent(this, old_type, step_type));
		}

	public List<StepConfiguration> getChildren()
		{
		return _children;
		}

	public void setChildren(List<StepConfiguration> children)
		{
		_children = children;
		}

    @SuppressWarnings("unused")  // used in UI
    public boolean isAncestorOf(StepConfiguration target)
        {
        if (_children == null)
            return false;
        if (_children.contains(target))
            return true;
        for (StepConfiguration child : _children)
            if (child.isAncestorOf(target))
                return true;
        return false;
        }

	/**
	 * Exists only for JSON serialization support. Should not be used for any other purpose.
	 */
	public Map<String, ValueSourceConfiguration> getSources()
		{
		return _sources;
		}

	/**
	 * Exists only for JSON serialization support. Should not be used for any other purpose.
	 */
	public void setSources(Map<String, ValueSourceConfiguration> sources)
		{
		if (_sources != null)
			throw new IllegalArgumentException("This method is only for deserialization. Cannot set the map again");
		_sources = sources;
		if (_sources != null)
			_sources.values().stream().filter(Objects::nonNull).forEach(source -> source.addChangeListener(getSourceListener()));
		}

	@JsonIgnore
	public Set<String> getSourceNames()
		{
		if (_sources == null)
			return Collections.emptySet();
		return _sources.keySet();
		}

	@JsonIgnore
	public ValueSourceConfiguration getSource(String name)
		{
		if (_sources == null)
			return null;
		return _sources.get(name);
		}

	public synchronized void addChild(StepConfiguration child)
		{
		if (_children == null)
			_children = new ArrayList<>();
		_children.add(child);
		notifyListeners(new ChildAddedEvent(this, child, _children.size() - 1));
		}

	@SuppressWarnings("unused") // used by GUI
	public synchronized void addChild(int index, StepConfiguration child)
		{
		if (_children == null)
			_children = new ArrayList<>();
		_children.add(index, child);
		notifyListeners(new ChildAddedEvent(this, child, index));
		}

	public MuseStep createStep() throws MuseInstantiationException
		{
		return createStep(new SimpleProject());
		}

	public MuseStep createStep(MuseProject project) throws MuseInstantiationException
		{
		if (project != null)
			return project.getStepFactory().createStep(this, project);

		return getDefaultStepFactory().createStep(this, null);
		}

	@Override
	public boolean equals(Object obj)
		{
		if (!(obj instanceof StepConfiguration))
			return false;

		StepConfiguration other = (StepConfiguration) obj;
		return _step_type.equals(other.getType())
			&& Objects.equals(_children, other.getChildren())
			&& Objects.equals(_sources, other.getSources())
			&& Objects.equals(getStepId(), other.getStepId());
		}

	@Override
	public void addSource(String name, ValueSourceConfiguration source)
		{
		if (_sources == null)
			_sources = new HashMap<>();
		if (_sources.containsKey(name))
			{
			if (Objects.equals(source, _sources.get(name)))
				return;
			throw new IllegalArgumentException(String.format("Can't add source %s, there is already a source with that naem", name));
			}
		_sources.put(name, source);
		source.addChangeListener(getSourceListener());
		notifyListeners(new NamedSourceAddedEvent(this, name, source));
		}

	@Override
	public ValueSourceConfiguration removeSource(String name)
		{
		if (_sources == null)
			return null;

		if (_sources.containsKey(name))
			{
			ValueSourceConfiguration removed = _sources.remove(name);
			if (removed != null)
				removed.removeChangeListener(getSourceListener());
			notifyListeners(new NamedSourceRemovedEvent(this, name, removed));
			return removed;
			}
		return null;
		}

	@Override
	public boolean renameSource(String old_name, String new_name)
		{
		if (_sources == null)
			return false;
		ValueSourceConfiguration source = _sources.remove(old_name);
		if (source == null)
			return false;
		_sources.put(new_name, source);
		notifyListeners(new NamedSourceRenamedEvent(this, new_name, old_name, source));
		return true;
		}

	@Override
	public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
		{
		if (_sources == null)
			return null;
		ValueSourceConfiguration old_source = _sources.remove(name);
		if (old_source == null)
			return null;
		_sources.put(name, new_source);
		old_source.removeChangeListener(getSourceListener());
		if (new_source != null)
			new_source.addChangeListener(getSourceListener());
		notifyListeners(new NamedSourceReplacedEvent(this, name, old_source, new_source));
		return old_source;
		}

	@SuppressWarnings("unused") // used by GUI
	public boolean hasChildren()
		{
		return _children != null && _children.size() > 0;
		}

	/**
	 * Returns the index of the child if removed, else -1 if not found.
	 */
	@SuppressWarnings({"unused", "UnusedReturnValue"}) // used by GUI
	public int removeChild(StepConfiguration child)
		{
		// note that we remove using object identity rather than equivalence, since a list can contain multiple identical StepConfigurations
		// _children.remove(child);
		int index = -1;
		for (int i = 0; i < _children.size(); i++)
			if (child == _children.get(i))
				{
				_children.remove(i);
				index = i;
				notifyListeners(new ChildRemovedEvent(this, child, i));
				break;
				}
		if (_children.size() == 0)
			_children = null;
		return index;
		}

	@JsonIgnore
	public void setMetadataField(String name, Object value)
		{
		_metadata.setMetadataField(name, value);
		}

	@SuppressWarnings("WeakerAccess")  // UI usage
	@JsonIgnore
	public void removeMetadataField(String name)
		{
		_metadata.removeMetadataField(name);
		}

	@JsonIgnore
	public Object getMetadataField(String name)
		{
		return _metadata.getMetadataField(name);
		}

	@Override
	public Set<String> getMetadataFieldNames()
		{
		return _metadata.getMetadataFieldNames();
		}

	public Map<String, Object> getMetadata()
		{
		return _metadata.getMap();
		}

	@SuppressWarnings("unused")  // required for JSON de/serialization
	public void setMetadata(Map<String, Object> metadata)
		{
		_metadata.setMap(metadata);
		}

	@SuppressWarnings("unused")
	@JsonIgnore
	public void setStepId(Long id)
		{
		setMetadataField(META_ID, id);
		}

	@SuppressWarnings("unused")
	@JsonIgnore
	public Long getStepId()
		{
		// upgrade old tests
		Object value = getMetadataField(META_ID_OLD);
		if (value != null)
			{
			_metadata.setMetadataField(META_ID, value);
			_metadata.removeMetadataField(META_ID_OLD);
			}

		value = getMetadataField(META_ID);
		if (value == null)
			return null;
		if (!(value instanceof Number))
			{
			LOG.error(String.format("Expected the 'id' metadata field to contain a number. Instead, found a %s. Returning null. ", value.getClass().getSimpleName()));
			return null;
			}
		return ((Number) value).longValue();
		}

	public StepConfiguration findParentOf(StepConfiguration target)
	    {
	    if (_children != null)
		    {
		    if (_children.contains(target))
		    	return this;
		    for (StepConfiguration child : _children)
			    {
			    StepConfiguration found_parent = child.findParentOf(target);
			    if (found_parent != null)
			    	return found_parent;
			    }
		    }
	    return null;
	    }

    public StepConfiguration findByStepId(Long step_id)
	    {
	    if (step_id.equals(getStepId()))
	    	return this;
	    if (_children != null)
		    {
		    for (StepConfiguration child : _children)
			    {
			    StepConfiguration found = child.findByStepId(step_id);
			    if (found != null)
			    	return found;
			    }
		    }
	    return null;

	    }

    public boolean contains(StepConfiguration descendent)
        {
        List<StepConfiguration> children = getChildren();
        if (children == null)
            return false;
        if (children.contains(descendent))
            return true;
        for (StepConfiguration child : children)
            if (child.contains(descendent))
                return true;
        return false;
        }

    @Override
    public String toString()
        {
        StringBuilder builder = new StringBuilder(_step_type);
        builder.append(": ");
        if (_sources != null && _sources.size() > 0)
            {
            boolean first = true;
            for (String name : _sources.keySet())
                {
                if (!first)
                    builder.append(", ");
                first = false;
                builder.append(name);
                builder.append("=");
                builder.append(_sources.get(name));
                }
            }
        return builder.toString();
        }

    private synchronized Set<ChangeEventListener> getListeners()
        {
        if (_listeners == null)
            {
            _listeners = new HashSet<>();
            if (_sources != null)
                for (ValueSourceConfiguration source : _sources.values())
                    source.addChangeListener(getSourceListener());
            }
        return _listeners;
        }

    private synchronized ChangeEventListener getSourceListener()
        {
        if (_source_listener == null)
            _source_listener = new SourceChangeListener();
        return _source_listener;
        }

    private void notifyListeners(ChangeEvent event)
        {
        for (ChangeEventListener listener : getListeners().toArray(new ChangeEventListener[0]))
            listener.changeEventRaised(event);
        }

    public void addChangeListener(ChangeEventListener listener)
        {
        getListeners().add(listener);
        }

    public boolean removeChangeListener(ChangeEventListener listener)
        {
        return getListeners().remove(listener);
        }

    @Override
    public TagContainer tags()
        {
        return _tags;
        }

    private String _step_type;
    private Map<String, ValueSourceConfiguration> _sources = null;
    private List<StepConfiguration> _children = null;
    private MetadataContainer _metadata = new MetadataContainer(this, this::notifyListeners);
    private TagContainer _tags = new MetadataTagContainer(_metadata);

    private transient Set<ChangeEventListener> _listeners;

    private transient ChangeEventListener _source_listener;

    public final static String META_DESCRIPTION = "_description";
    @SuppressWarnings("unused")
    public final static String META_DESCRIPTION_OLD = "description";
    public final static String META_ID = "_id";
    @SuppressWarnings("WeakerAccess")  // IDE uses this to auto-upgrade
    public final static String META_ID_OLD = "id";
    public final static String META_TAGS = "_tags";

    private static StepFactory getDefaultStepFactory()
        {
        if (DEFAULT == null)
            {
            TypeLocator locator = new TypeLocator((MuseProject)null);
            DEFAULT = new CompoundStepFactory(new ClasspathStepFactory(locator));
            }
        return DEFAULT;
        }

    private class SourceChangeListener extends ValueSourceChangeObserver
        {
        @Override
        public void changeEventRaised(ChangeEvent event)
            {
            if (!(event instanceof ValueSourceChangeEvent))
                return;
            ValueSourceChangeEvent e = (ValueSourceChangeEvent) event;
            ValueSourceConfiguration source = e.getSource();
            String source_name = null;
            if (_sources != null)
                for (String name : _sources.keySet())
                    if (source == _sources.get(name))
                        {
                        source_name = name;
                        break;
                        }
            if (source_name == null)
                LOG.error("A change event for a value source received, but this step does not contain this source. Someone forget to de-register a listener?");
            else
                notifyListeners(new SourceChangedEvent(StepConfiguration.this, e, source_name));
            }
        }

    @SuppressWarnings("unused")  // public API
    public static StepConfiguration create(MuseProject project, String step_type)
	    {
	    StepConfiguration config = new StepConfiguration(step_type);
	    config.setMetadataField(StepConfiguration.META_ID, StepIdGenerator.get(project).generateLongId());
	    return config;
	    }

    @SuppressWarnings("unused")  // public API
    public static StepConfiguration create(MuseProject project, StepDescriptor descriptor)
	    {
	    StepConfiguration config = new StepConfiguration(descriptor.getType());
	    for (SubsourceDescriptor source_descriptor : descriptor.getSubsourceDescriptors())
            if (!source_descriptor.isOptional())
                config.addSource(source_descriptor.getName(), source_descriptor.getDefault());
        config.setMetadataField(StepConfiguration.META_ID, StepIdGenerator.get(project).generateLongId());
	    return config;
	    }

    @SuppressWarnings("unused")  // public API
    public static StepConfiguration copy(StepConfiguration old, MuseProject project)
	    {
	    StepConfiguration new_config = Copy.withJavaSerialization(old);
        setNewStepIds(new_config, StepIdGenerator.get(project));
	    return new_config;
	    }

    private static void setNewStepIds(StepConfiguration step, StepIdGenerator generator)
        {
        step.setMetadataField(StepConfiguration.META_ID, generator.generateLongId());
        if (step._children != null)
            for (StepConfiguration child : step.getChildren())
                setNewStepIds(child, generator);
        }

    private static StepFactory DEFAULT = null;

    private final static Logger LOG = LoggerFactory.getLogger(StepConfiguration.class);
    }

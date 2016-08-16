package org.musetest.core.step;

import com.fasterxml.jackson.annotation.*;
import org.musetest.builtins.value.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.descriptor.*;
import org.musetest.core.step.events.*;
import org.musetest.core.step.events.TypeChangeEvent;
import org.musetest.core.step.factory.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;
import org.musetest.core.values.descriptor.*;
import org.musetest.core.values.events.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepConfiguration implements Serializable, ContainsNamedSources
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
                setSource(source_descriptor.getName(), ValueSourceConfiguration.forType(StringValueSource.TYPE_ID));
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

    public Map<String, ValueSourceConfiguration> getSources()
        {
        return _sources;
        }

    public void setSources(Map<String, ValueSourceConfiguration> sources)
        {
        if (_sources != null)
            for (ValueSourceConfiguration source : _sources.values())
                if (source != null)
                    source.removeChangeListener(getSourceListener());
        _sources = sources;
        if (_sources != null)
            for (ValueSourceConfiguration source : _sources.values())
                if (source != null)
                    source.addChangeListener(getSourceListener());
        }

    @JsonIgnore
    public ValueSourceConfiguration getSource(String name)
        {
        if (_sources == null)
            return null;
        return _sources.get(name);
        }

    public void addChild(StepConfiguration child)
        {
        if (_children == null)
            _children = new ArrayList<>();
        _children.add(child);
        }

    @SuppressWarnings("unused") // used by GUI
    public void addChild(int index, StepConfiguration child)
        {
        if (_children == null)
            _children = new ArrayList<>();
        _children.add(index, child);
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
            && Objects.equals(_sources, other.getSources());
        }

    public void setSource(String name, ValueSourceConfiguration source)
        {
        if (_sources == null)
            _sources = new HashMap<>();
        ValueSourceConfiguration old_source = _sources.get(name);
        if (!Objects.equals(source, old_source))
            {
            if (old_source != null)
                old_source.removeChangeListener(getSourceListener());
            _sources.put(name, source);
            if (source != null)
                source.addChangeListener(getSourceListener());
            notifyListeners(new SourceAddedOrRemovedEvent(this, name, old_source, source));
            }
        }

    @SuppressWarnings("unused") // used by GUI
    public boolean hasChildren()
        {
        return _children != null && _children.size() > 0;
        }

    @SuppressWarnings("unused") // used by GUI
    public void removeChild(StepConfiguration child)
        {
        // note that we remove using object identity rather than equivalence, since a list can contain multiple identical StepConfigurations
        // _children.remove(child);
        for (int i = 0; i < _children.size(); i++)
            if (child == _children.get(i))
                _children.remove(i);
        if (_children.size() == 0)
            _children = null;
        }

    @JsonIgnore
    public void setMetadataField(String name, Object value)
        {
        if (_metadata == null)
            _metadata = new HashMap<>();
        Object old_value = _metadata.get(name);
        _metadata.put(name, value);
        if (_metadata.size() == 0)
            _metadata = null;

        notifyListeners(new MetadataChangeEvent(this, name, old_value, value));
        }

    @JsonIgnore
    public Object getMetadataField(String name)
        {
        if (_metadata == null)
            return null;
        return _metadata.get(name);
        }

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

    private synchronized Set<StepConfigurationChangeListener> getListeners()
        {
        if (_listeners == null)
            {
            _listeners = new HashSet<>();
            for (ValueSourceConfiguration source : _sources.values())
                source.addChangeListener(getSourceListener());
            }
        return _listeners;
        }

    private synchronized ValueSourceChangeListener getSourceListener()
        {
        if (_source_listener == null)
            _source_listener = new SourceChangeListener();
        return _source_listener;
        }

    private void notifyListeners(StepChangeEvent event)
        {
        for (StepConfigurationChangeListener listener : getListeners())
            listener.changed(event);
        }

    public void addChangeListener(StepConfigurationChangeListener listener)
        {
        getListeners().add(listener);
        }

    public void removeChangeListener(StepConfigurationChangeListener listener)
        {
        getListeners().remove(listener);
        }

    private String _step_type;
    private Map<String, ValueSourceConfiguration> _sources = new HashMap<>();
    private List<StepConfiguration> _children = null;
    private Map<String, Object> _metadata = null;

    private transient Set<StepConfigurationChangeListener> _listeners;

    private transient ValueSourceChangeListener _source_listener;

    public final static String META_DESCRIPTION = "description";

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
        public void changed(ValueSourceChangeEvent event)
            {
            ValueSourceConfiguration source = event.getSource();
            String source_name = null;
            for (String name : _sources.keySet())
                if (source == _sources.get(name))
                    {
                    source_name = name;
                    break;
                    }
            if (source_name == null)
                LOG.error("A change event for a value source received, but this step does not contain this source. Someone forget to de-register a listener?");
            else
                notifyListeners(new SourceChangedEvent(StepConfiguration.this, event, source_name));
            }
        }

    private static StepFactory DEFAULT = null;

    private final static Logger LOG = LoggerFactory.getLogger(StepConfiguration.class);
    }

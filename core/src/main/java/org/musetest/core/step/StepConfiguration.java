package org.musetest.core.step;

import com.fasterxml.jackson.annotation.*;
import org.musetest.core.*;
import org.musetest.core.project.*;
import org.musetest.core.resource.*;
import org.musetest.core.step.factory.*;
import org.musetest.core.util.*;
import org.musetest.core.values.*;

import java.io.*;
import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class StepConfiguration implements Serializable
    {
    public StepConfiguration()
        {
        }

    public StepConfiguration(String step_type)
        {
        _step_type = step_type;
        }

    public String getType()
        {
        return _step_type;
        }

    public void setType(String step_type)
        {
        _step_type = step_type;
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
        _sources = sources;
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

    public ValueSourceConfiguration getSourceConfiguration(String name)
        {
        if (_sources == null)
            return null;
        return _sources.get(name);
        }

    public void addSource(String name, ValueSourceConfiguration source)
        {
        if (_sources == null)
            _sources = new HashMap<>();
        _sources.put(name, source);
        }

    public boolean hasChildren()
        {
        return _children != null && _children.size() > 0;
        }

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
        _metadata.put(name, value);
        if (_metadata.size() == 0)
            _metadata = null;
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

    private String _step_type;
    private Map<String, ValueSourceConfiguration> _sources = new HashMap<>();
    private List<StepConfiguration> _children = null;
    private Map<String, Object> _metadata = null;

    public final static String META_DESCRIPTION = "description";

    static StepFactory getDefaultStepFactory()
        {
        if (DEFAULT == null)
            {
            TypeLocator locator = new TypeLocator((MuseProject)null);
            DEFAULT = new CompoundStepFactory(new ClasspathStepFactory(locator));
            }
        return DEFAULT;
        }

    private static StepFactory DEFAULT = null;
    }

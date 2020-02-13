package org.museautomation.core.variables;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;
import org.museautomation.core.util.*;
import org.museautomation.core.values.*;
import org.museautomation.core.values.events.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("variables")
public class VariableList extends BaseMuseResource
    {
    public Map<String, ValueSourceConfiguration> getVariables()
        {
        return Collections.unmodifiableMap(_variables);
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setVariables(Map<String, ValueSourceConfiguration> elements)
        {
        _variables = elements;
        }

    public void addVariable(String id, ValueSourceConfiguration element)
        {
        if (_variables == null)
            _variables = new HashMap<>();
        _variables.put(id, element);
        }

    @Override
    public ResourceType getType()
        {
        return new VariableListResourceType();
        }

    @SuppressWarnings("unused") // used by UI
    public ContainsNamedSources namedElementLocators()
        {
        return new NamedSources();
        }

    private Map<String, ValueSourceConfiguration> _variables = new HashMap<>();

    /**
     * Provides a facade for the element locator sources in the page. This facade makes the element/locator
     * pairs editable with a ValueSourceMapEditor.
     */
    private class NamedSources implements ContainsNamedSources
        {
        @Override
        public ValueSourceConfiguration getSource(String name)
            {
            return _variables.get(name);
            }

        @Override
        public void addSource(String name, ValueSourceConfiguration source)
            {
            ValueSourceConfiguration existing = _variables.get(name);
            if (existing != null)
                throw new IllegalArgumentException(String.format("Source %s already exists in list. Cannot add it.", name));
            _variables.put(name, source);
            raiseEvent(new NamedSourceAddedEvent(this, name, source));
            }

        @Override
        public ValueSourceConfiguration removeSource(String name)
            {
            ValueSourceConfiguration removed = _variables.remove(name);
            if (removed == null)
                throw new IllegalArgumentException(String.format("Source %s does not exist in the list. Cannot remove it.", name));
            raiseEvent(new NamedSourceRemovedEvent(this, name, removed));
            return removed;
            }

        @Override
        public boolean renameSource(String old_name, String new_name)
            {
            if (_variables.get(new_name) != null)
                throw new IllegalArgumentException(String.format("Source %s already exists in list. Cannot rename %s to %s", new_name, old_name, new_name));
            ValueSourceConfiguration renamed = _variables.remove(old_name);
            if (renamed == null)
                throw new IllegalArgumentException(String.format("Source %s does not exist in list. Cannot rename it", old_name));

            _variables.put(new_name, renamed);
            raiseEvent(new NamedSourceRenamedEvent(this, new_name, old_name, renamed));
            return true;
            }

        @Override
        public ValueSourceConfiguration replaceSource(String name, ValueSourceConfiguration new_source)
            {
            ValueSourceConfiguration replaced = _variables.get(name);
            if (replaced == null)
                throw new IllegalArgumentException(String.format("Source %s does not exist in list. Cannot replace it", name));

            _variables.put(name, new_source);
            raiseEvent(new NamedSourceReplacedEvent(this, name, replaced, new_source));
            return replaced;
            }

        @Override
        public Set<String> getSourceNames()
            {
            return _variables.keySet();
            }

        @Override
        public void addChangeListener(ChangeEventListener listener)
            {
            if (_listeners.contains(listener))
                return;
            _listeners.add(listener);
            }

        @Override
        public boolean removeChangeListener(ChangeEventListener listener)
            {
            return _listeners.remove(listener);
            }

        private void raiseEvent(ChangeEvent event)
            {
            for (ChangeEventListener listener : _listeners)
                listener.changeEventRaised(event);
            }

        List<ChangeEventListener> _listeners = new ArrayList<>();
        }

    public final static String TYPE_ID = VariableList.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class VariableListResourceType extends ResourceType
        {
        public VariableListResourceType()
            {
            super(TYPE_ID, "Variable List", VariableList.class);
            }
        }
    }



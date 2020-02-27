package org.museautomation.core.task.state;

import org.museautomation.core.*;
import org.museautomation.core.resource.*;
import org.museautomation.core.resource.types.*;

import java.util.*;

/**
 * Describes the properties of a specific "user state" - a state between tasks in a workflow, as well as begin and end states.
 * It is much like a class, in that it describes instances of the user state - which are InterTaskState objects.
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 * @see InterTaskState
 */
@MuseTypeId("state-definition")
public class StateDefinition extends BaseMuseResource
    {
    public String getDisplayName()
        {
        return _display_name;
        }

    public void setDisplayName(String display_name)
        {
        _display_name = display_name;
        }

    public List<StateValueDefinition> getValues()
        {
        return _values;
        }

    public void setValues(List<StateValueDefinition> values)
        {
        _values = values;
        }

    @Override
    public ResourceType getType()
        {
        return new StateDefinitionResourceType();
        }

    public StateValueDefinition getValue(String name)
        {
        for (StateValueDefinition value : _values)
            if (name.equals(value.getName()))
                return value;
        return null;
        }

    private String _display_name;
    private List<StateValueDefinition> _values;

    public final static String TYPE_ID = StateDefinition.class.getAnnotation(MuseTypeId.class).value();

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    public static class StateDefinitionResourceType extends ResourceType
        {
        public StateDefinitionResourceType()
            {
            super(TYPE_ID, "State Definition", StateDefinition.class);
            }

        @Override
        public MuseResource create()
            {
            return new StateDefinition();
            }
        }
    }
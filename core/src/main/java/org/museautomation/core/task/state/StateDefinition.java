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

    public List<StateValue> getValues()
        {
        return _values;
        }

    public void setValues(List<StateValue> values)
        {
        _values = values;
        }

    @Override
    public ResourceType getType()
        {
        return new StateDefinitionResourceType();
        }

    private String _display_name;
    private List<StateValue> _values;

    @SuppressWarnings("WeakerAccess")  // discovered and instantiated by reflection (see class ResourceTypes)
    static class StateDefinitionResourceType extends ResourceType
        {
        public StateDefinitionResourceType()
            {
            super("state", "State", StateDefinition.class);
            }

        @Override
        public MuseResource create()
            {
            return new StateDefinition();
            }
        }
    }
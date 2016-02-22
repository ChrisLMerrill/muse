package org.musetest.core.variables;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;
import org.musetest.core.values.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("variables")
public class VariableList implements MuseResource
    {
    public Map<String, ValueSourceConfiguration> getVariables()
        {
        return _variables;
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
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    private Map<String, ValueSourceConfiguration> _variables = new HashMap<>();
    private ResourceMetadata _metadata = new ResourceMetadata(new VariableListType());

    @SuppressWarnings("unused")  // discovered via reflection
    public static class VariableListType extends ResourceType
        {
        public VariableListType()
            {
            super(VariableList.class.getAnnotation(MuseTypeId.class).value(), "Variable List", VariableList.class);
            }
        }
    }



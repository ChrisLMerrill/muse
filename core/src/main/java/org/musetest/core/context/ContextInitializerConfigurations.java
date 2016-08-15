package org.musetest.core.context;

import org.musetest.core.*;
import org.musetest.core.resource.*;
import org.musetest.core.resource.types.*;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@MuseTypeId("context-initializers")
public class ContextInitializerConfigurations implements MuseResource
    {
    public List<VariableListContextInitializerConfiguration> getVariableListInitializers()
        {
        return _var_lists;
        }

    @SuppressWarnings("unused")  // required for Json de/serialization
    public void setVariableListInitializers(List<VariableListContextInitializerConfiguration> var_lists)
        {
        _var_lists = var_lists;
        }

    @Override
    public ResourceMetadata getMetadata()
        {
        return _metadata;
        }

    public void addVariableListCondition(VariableListContextInitializerConfiguration config)
        {
        _var_lists.add(config);
        }

    private List<VariableListContextInitializerConfiguration> _var_lists = new ArrayList<>();
    private ResourceMetadata _metadata = new ResourceMetadata(new ContextInitializersConfigurationType());

    @SuppressWarnings("unused")  // discovered via reflection
    public static class ContextInitializersConfigurationType extends ResourceType
        {
        public ContextInitializersConfigurationType()
            {
            super(ContextInitializerConfigurations.class.getAnnotation(MuseTypeId.class).value(), "Context Initializers", ContextInitializerConfigurations.class);
            }
        }
    }


